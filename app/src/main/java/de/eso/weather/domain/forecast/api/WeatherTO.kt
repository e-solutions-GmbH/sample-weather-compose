package de.eso.weather.domain.forecast.api

import de.eso.weather.domain.shared.api.Location

data class WeatherTO(val weather: String, val location: Location)
