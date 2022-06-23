package de.eso.weather.ui.alert

import androidx.lifecycle.ViewModel
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable

class AlertViewModel(
    weatherAlertService: WeatherAlertService,
    locationService: LocationService,
    locationId: String
) : ViewModel() {

    val location: Observable<Location> = locationService.getLocation(locationId).toObservable()

    val weatherAlerts: Observable<List<AlertListItem>> = location
        .switchMap {
            weatherAlertService
                .getWeatherAlerts(it)
                .map { alerts -> alerts.map { alert -> AlertListItem(alert) } }
        }
}
