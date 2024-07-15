package de.eso.weather.domain.forecast.platform

import android.util.Log
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.forecast.service.ForecastProvider
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class WeatherForecastProvider : ForecastProvider {
    private val weatherConditions = listOf("Sunshine", "Rain", "Cloudy", "Snow", "Thunderstorm")

    // imagine a call to an external API to get the forecast - in this example app we will just generate a random forecast
    // this is our call to external library with a synchronous interface
    private fun getWeatherCondition(location: Location): String = weatherConditions.random()
        .also { Log.i("Weather", "[Thread: ${Thread.currentThread()}] Weather provided externally for $location is: $it") }

    // map the sync interface to an Rx interface
    override fun getCurrentWeather(location: Location): Flow<WeatherTO> =
        flowOf(WeatherTO(getWeatherCondition(location), location))
            .onEach { Log.i("Weather", "[Thread: ${Thread.currentThread()}] Weather in ${it.location} is: ${it.weather}") }
}
