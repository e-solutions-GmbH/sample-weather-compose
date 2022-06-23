package de.eso.weather.domain.location.service

import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

class LocationServiceImpl : LocationService, LocationsReceiver {

    private val locationsSubject: Subject<List<Location>> = BehaviorSubject.create()

    override val availableLocations = locationsSubject

    override fun getLocation(id: String): Maybe<Location> =
        locationsSubject.firstOrError().flatMapObservable { Observable.fromIterable(it) }
            .filter { it.id == id }
            .firstElement()

    override fun queryLocations(locationName: String): Single<List<Location>> =
        locationsSubject.firstOrError().map { locations ->
            locations.filter { it.name.contains(locationName.trim(), ignoreCase = true) }
        }

    override fun updateAvailableLocations(locations: List<Location>) {
        locationsSubject.onNext(locations.sortedBy { it.name })
    }
}
