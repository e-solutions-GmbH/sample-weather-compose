package de.eso.weather.ui.forecast

import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location

data class ForecastViewState(
    val activeLocation: Location,
    val weather: WeatherTO
)

val EmptyForecastViewState = ForecastViewState(
    activeLocation = Location(name = "Empty"),
    weather = WeatherTO(weather = "none", location = Location(name = "Empty"))
)

fun checkNotEmptyForecastViewState(viewState: ForecastViewState): ForecastViewState {
    if (viewState == EmptyForecastViewState) {
        throw IllegalStateException("Using EmptyForecastViewState")
    } else {
        return viewState
    }
}
