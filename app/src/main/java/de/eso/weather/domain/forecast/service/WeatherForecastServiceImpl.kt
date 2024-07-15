package de.eso.weather.domain.forecast.service

import android.util.Log
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Scheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlin.time.Duration.Companion.seconds

class WeatherForecastServiceImpl(
    private val forecastProvider: ForecastProvider,
    private val scope: CoroutineScope = MainScope()
) : WeatherForecastService {

    private val timer = flow {
        while(true) {
            delay(TIMER_INTERVAL_SECONDS)
            emit(null)
        }
    }
        .conflate()
        .shareIn(scope, SharingStarted.WhileSubscribed(0,0), 0)

    private val locationFlows = mutableMapOf<Location, Flow<WeatherTO>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWeather(location: Location) =
        locationFlows.computeIfAbsent(location) {
            timer.flatMapLatest { forecastProvider.getCurrentWeather(location) }
                .onEach {
                    Log.i("Weather", "[Thread: ${Thread.currentThread()}] Update Weather for $location is: $it")
                }
                .shareIn(scope, SharingStarted.WhileSubscribed(), 1)
        }

    companion object {
        private const val TIMER_INTERVAL_SECONDS = 3000L
    }
}
