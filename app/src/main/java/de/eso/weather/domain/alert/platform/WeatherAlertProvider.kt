package de.eso.weather.domain.alert.platform

import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.domain.shared.platform.Locations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WeatherAlertProvider(private val randomBoolSupplier: RandomBooleanSupplier, private val scope: CoroutineScope,) : AlertProvider {
    val _alerts = MutableSharedFlow<List<WeatherAlertTO>>()
    override val alerts = _alerts.asSharedFlow()

    init {
        scope.launch {
            while(true) {
                delay(UPDATE_INTERVAL_SECONDS)
                _alerts.emit(Locations.knownLocations.flatMap { location -> getAlertsForSingleLocation(location) })
            }
        }
    }

    private fun getAlertsForSingleLocation(location: Location): List<WeatherAlertTO> =
        possibleAlerts
            .filter { randomBoolSupplier.next() }
            .map { WeatherAlertTO(it, location) }
            .ifEmpty { listOf(WeatherAlertTO(NO_ALERT, location)) }

    companion object {
        private const val UPDATE_INTERVAL_SECONDS: Long = 3
        private const val NO_ALERT = "No warning currently, the weather is fine!"
        private const val POLLEN_ALERT = "Pollen"
        private const val RADIATION_ALERT = "Radiation"
        private const val OZONE_ALERT = "Ozone"

        private val possibleAlerts = listOf(
            POLLEN_ALERT,
            RADIATION_ALERT,
            OZONE_ALERT
        )
    }
}
