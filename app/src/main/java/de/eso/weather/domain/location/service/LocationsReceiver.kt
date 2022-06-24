package de.eso.weather.domain.location.service

import de.eso.weather.domain.shared.api.Location

interface LocationsReceiver {

    fun updateAvailableLocations(locations: List<Location>)
}
