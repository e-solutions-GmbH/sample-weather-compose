package de.eso.weather.ui.forecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Optional


@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModel(
    private val weatherForecastService: WeatherForecastService,
    favoriteLocationsRepository: FavoriteLocationsRepository
) : ViewModel() {
    var forecastViewState by mutableStateOf(EmptyForecastViewState)
    var forecastSavedLocationsViewState by mutableStateOf<List<ForecastViewState>>(emptyList())

    init {
        viewModelScope.launch {
            favoriteLocationsRepository
                .activeLocation
                .flatMapLatest { location ->
                    if (!location.isPresent) {
                        flowOf(EmptyForecastViewState)
                    } else {
                        weatherForecastService
                            .getWeather(location.get())
                            .map { weatherTO -> ForecastViewState(location.get(), weatherTO) }
                    }
                }
                .filter { it != EmptyForecastViewState }
                //.flowOn(mainScheduler)
                .collect {
                    forecastViewState = it
                }
            favoriteLocationsRepository.activeLocation.onStart { emit(Optional.empty()) }
                .combine(favoriteLocationsRepository.savedLocations) { activeLocation, savedLocations ->
                    if (activeLocation.isPresent) {
                        savedLocations - activeLocation.get()
                    } else {
                        savedLocations
                    }
                }
                .flatMapLatest { locations ->
                    val weatherForLocations = locations.map { location ->
                        weatherForecastService
                            .getWeather(location)
                            .map { weatherTO -> ForecastViewState(location, weatherTO) }
                    }

                    combine(weatherForLocations) { it }
                }
                //.observeOn(mainScheduler)
                .collect {
                    forecastSavedLocationsViewState = it.asList()
                }
        }
    }
}
