package de.eso.weather.domain.forecast.service

import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Single

interface ForecastProvider {
    fun getCurrentWeather(location: Location): Single<WeatherTO>
}
