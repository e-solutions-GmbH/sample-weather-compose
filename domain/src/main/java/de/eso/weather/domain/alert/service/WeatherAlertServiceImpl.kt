package de.eso.weather.domain.alert.service

import android.util.Log
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

class WeatherAlertServiceImpl : WeatherAlertService, AlertReceiver {

    private val currentAlerts: Subject<List<WeatherAlertTO>> =
        BehaviorSubject.createDefault(emptyList())

    init {
        Log.i("Weather", "WeatherAlertService started")
    }

    override fun getWeatherAlerts(location: Location): Observable<List<WeatherAlertTO>> =
        currentAlerts
            .map { it.filter { alert -> alert.location == location } }
            .distinctUntilChanged()
            .doAfterNext { Log.i("Weather", "Weather Alerts for $location are: $it") }

    override fun updateAlerts(alerts: List<WeatherAlertTO>) {
        currentAlerts.onNext(alerts)
    }
}
