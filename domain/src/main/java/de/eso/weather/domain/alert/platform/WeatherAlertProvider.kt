package de.eso.weather.domain.alert.platform

import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.domain.shared.platform.Locations
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit

class WeatherAlertProvider(private val scheduler: Scheduler, private val randomBoolSupplier: RandomBooleanSupplier) : AlertProvider {

    override fun alerts(): Observable<List<WeatherAlertTO>> =
        Observable.interval(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS, scheduler)
            .map { Locations.knownLocations.flatMap { location -> getAlertsForSingleLocation(location) } }

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
