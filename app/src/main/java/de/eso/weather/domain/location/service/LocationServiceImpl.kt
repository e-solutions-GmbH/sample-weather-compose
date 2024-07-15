package de.eso.weather.domain.location.service

import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.take

class LocationServiceImpl : LocationService, LocationsReceiver {

    private val locationsFlow = MutableSharedFlow<List<Location>>(1)

    override val availableLocations = locationsFlow.asSharedFlow()

    override fun getLocation(id: String): Flow<Location> =
        locationsFlow
            .take(1)
            .mapNotNull { locations -> locations.find { it.id == id } }

    override fun queryLocations(locationName: String): Flow<List<Location>> =
        locationsFlow
            .take(1)
            .map { locations ->
                locations.filter { it.name.contains(locationName.trim(), ignoreCase = true) }
            }

    override fun updateAvailableLocations(locations: List<Location>) {
        if (!locationsFlow.tryEmit(locations.sortedBy { it.name })) {
            error("Unable to emit available locations")
        }
    }
}
