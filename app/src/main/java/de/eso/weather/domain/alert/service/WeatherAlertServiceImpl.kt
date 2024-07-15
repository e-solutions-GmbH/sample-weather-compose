package de.eso.weather.domain.alert.service

import android.util.Log
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class WeatherAlertServiceImpl : WeatherAlertService, AlertReceiver {

    private val currentAlerts = MutableStateFlow(emptyList<WeatherAlertTO>())

    init {
        Log.i("Weather", "WeatherAlertService started")
    }

    override fun getWeatherAlerts(location: Location): Flow<List<WeatherAlertTO>> =
        currentAlerts
            .map { it.filter { alert -> alert.location == location } }
            .distinctUntilChanged()
            .onEach { Log.i("Weather", "Weather Alerts for $location are: $it") }

    override fun updateAlerts(alerts: List<WeatherAlertTO>) {
        currentAlerts.tryEmit(alerts)
    }
}
