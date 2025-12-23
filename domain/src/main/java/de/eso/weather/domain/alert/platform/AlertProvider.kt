package de.eso.weather.domain.alert.platform

import de.eso.weather.domain.alert.api.WeatherAlertTO
import io.reactivex.rxjava3.core.Observable

interface AlertProvider {
    fun alerts(): Observable<List<WeatherAlertTO>>
}
