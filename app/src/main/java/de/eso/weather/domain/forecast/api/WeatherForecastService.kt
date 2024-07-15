package de.eso.weather.domain.forecast.api

import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

/** This is the public API of our Domain API, it is provided to the UI.*/
interface WeatherForecastService {
    fun getWeather(location: Location): Flow<WeatherTO>
}
