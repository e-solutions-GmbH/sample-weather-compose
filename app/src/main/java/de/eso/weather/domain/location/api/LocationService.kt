package de.eso.weather.domain.location.api

import de.eso.weather.domain.shared.api.Location
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface LocationService {
    val availableLocations: Flow<List<Location>>

    fun getLocation(id: String): Flow<Location>

    fun queryLocations(locationName: String): Flow<List<Location>>
}
