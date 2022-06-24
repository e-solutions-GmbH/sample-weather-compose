package de.eso.weather.domain.forecast.service

import android.util.Log
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit.SECONDS

class WeatherForecastServiceImpl(
    scheduler: Scheduler,
    private val forecastProvider: ForecastProvider
) : WeatherForecastService {

    private val timer = Observable
        .interval(0, TIMER_INTERVAL_SECONDS, SECONDS, scheduler)
        .share()

    private val locationObservables = mutableMapOf<Location, Observable<WeatherTO>>()

    override fun getWeather(location: Location) =
        locationObservables.computeIfAbsent(location) {
            timer
                .switchMapSingle { forecastProvider.getCurrentWeather(location) }
                .doOnNext { Log.i("Weather", "[Thread: ${Thread.currentThread()}] Update Weather for $location is: $it") }
                .share()
                .doOnDispose { locationObservables.remove(location) }
        }

    companion object {
        private const val TIMER_INTERVAL_SECONDS: Long = 3
    }
}
