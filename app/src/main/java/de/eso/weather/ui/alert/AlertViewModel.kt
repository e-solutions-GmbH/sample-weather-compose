package de.eso.weather.ui.alert

import androidx.lifecycle.ViewModel
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class AlertViewModel(
    weatherAlertService: WeatherAlertService,
    locationService: LocationService,
    locationId: String
) : ViewModel() {

    val location: Flow<Location> = locationService.getLocation(locationId)

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherAlerts: Flow<List<AlertListItem>> =
        locationService.getLocation(locationId).flatMapLatest {
            weatherAlertService
                .getWeatherAlerts(it)
                .map { alerts -> alerts.map { alert -> AlertListItem(alert) } }
        }

}
