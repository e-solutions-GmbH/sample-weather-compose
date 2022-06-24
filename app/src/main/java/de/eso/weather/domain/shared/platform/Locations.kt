package de.eso.weather.domain.shared.platform

import de.eso.weather.domain.shared.api.Location

/**
 * These locations simulate coming from an external service, that's why they have a fixed ID.
 */
object Locations {
    val knownLocations = listOf(
        Location(name = "Ammerndorf", id = "1-1-1-1-1"),
        Location(name = "Berlin", id = "2-2-2-2-2"),
        Location(name = "Cadolzburg", id = "3-3-3-3-3"),
        Location(name = "Erlangen", id = "4-4-4-4-4"),
        Location(name = "Fürth", id = "5-5-5-5-5"),
        Location(name = "München", id = "6-6-6-6-6"),
        Location(name = "Nürnberg", id = "7-7-7-7-7"),
        Location(name = "Zirndorf", id = "8-8-8-8-8")
    )
}
