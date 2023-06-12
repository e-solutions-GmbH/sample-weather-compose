package de.eso.weather.ui.forecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo


class ForecastViewModel(
    private val weatherForecastService: WeatherForecastService,
    private val favoriteLocationsRepository: FavoriteLocationsRepository,
    mainScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {
    var forecastViewState by mutableStateOf(ForecastViewState())
    var forecastSavedLocationsViewState by mutableStateOf<List<ForecastViewState>>(emptyList())

    private val disposables = CompositeDisposable()

    init {
        favoriteLocationsRepository
            .activeLocation
            .switchMap { location ->
                if (!location.isPresent) {
                    Observable.just(ForecastViewState())
                } else {
                    Observable
                        .just(ForecastViewState(location.get()))
                        .mergeWith(
                            weatherForecastService
                                .getWeather(location.get())
                                .map { weatherTO -> ForecastViewState(location.get(), weatherTO) }
                        )
                }
            }
            .observeOn(mainScheduler)
            .subscribe {
                forecastViewState = it
            }
            .addTo(disposables)

        favoriteLocationsRepository
            .savedLocations
            .switchMap { locations ->
                val weatherForLocations = locations.map {
                    location -> weatherForecastService
                    .getWeather(location)
                    .map { weatherTO -> ForecastViewState(location, weatherTO) }
                }.toTypedArray()

                Observable.combineLatestArray(weatherForLocations) {
                    it.toList() as List<ForecastViewState>
                }
            }
            .observeOn(mainScheduler)
            .subscribe {
                forecastSavedLocationsViewState = it
            }
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
    }
}
