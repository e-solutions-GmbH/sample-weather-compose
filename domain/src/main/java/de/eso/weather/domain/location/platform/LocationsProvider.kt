package de.eso.weather.domain.location.platform

import de.eso.weather.domain.location.service.LocationsReceiver
import de.eso.weather.domain.shared.platform.Locations
import de.eso.weather.domain.util.Logger

class LocationsProvider(
    private val locationsReceiver: LocationsReceiver,
    logger: Logger,
) {

    init {
        readLocationsFromExternalSource()
    }

    private fun readLocationsFromExternalSource() {
        // imagine a call to an external API which would asynchronously send locations
        locationsReceiver.updateAvailableLocations(Locations.knownLocations)
    }
}
