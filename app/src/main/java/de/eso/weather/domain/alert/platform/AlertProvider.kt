package de.eso.weather.domain.alert.platform

import de.eso.weather.domain.alert.api.WeatherAlertTO
import kotlinx.coroutines.flow.Flow

interface AlertProvider {
    val alerts: Flow<List<WeatherAlertTO>>
}
