package de.eso.weather.domain.alert.api

import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable

interface WeatherAlertService {

    fun getWeatherAlerts(location: Location): Observable<List<WeatherAlertTO>>
}
