package de.eso.weather.domain.location.api

import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocationService {
    val availableLocations: Observable<List<Location>>

    fun getLocation(id: String): Maybe<Location>

    fun queryLocations(locationName: String): Single<List<Location>>
}
