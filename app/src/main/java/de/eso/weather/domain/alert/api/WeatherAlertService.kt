package de.eso.weather.domain.alert.api

import de.eso.weather.domain.shared.api.Location
import kotlinx.coroutines.flow.Flow

interface WeatherAlertService {

    fun getWeatherAlerts(location: Location): Flow<List<WeatherAlertTO>>
}
