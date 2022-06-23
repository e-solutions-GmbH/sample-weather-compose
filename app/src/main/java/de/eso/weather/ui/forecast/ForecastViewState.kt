package de.eso.weather.ui.forecast

import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location

data class ForecastViewState(
    val activeLocation: Location? = null,
    val weather: WeatherTO? = null
)
