package de.eso.weather.domain.shared.api

import java.util.UUID

data class Location(val name: String, val id: String = UUID.randomUUID().toString())
