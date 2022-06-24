package de.eso.weather.domain.alert.api

import de.eso.weather.domain.shared.api.Location

data class WeatherAlertTO(val alert: String, val location: Location)
