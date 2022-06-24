package de.eso.weather.domain.location.platform

import de.eso.weather.domain.location.service.LocationsReceiver
import de.eso.weather.domain.shared.platform.Locations

class LocationsProvider(private val locationsReceiver: LocationsReceiver) {

    init {
        readLocationsFromExternalSource()
    }

    private fun readLocationsFromExternalSource() {
        // imagine a call to an external API which would asynchronously send locations
        locationsReceiver.updateAvailableLocations(Locations.knownLocations)
    }
}
