package de.eso.weather.domain.location.api

import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Observable
import java.util.Optional

interface FavoriteLocationsRepository {

    val savedLocations: Observable<List<Location>>

    val activeLocation: Observable<Optional<Location>>

    fun saveLocation(location: Location)

    fun deleteLocation(location: Location)

    fun setActive(location: Location)
}
