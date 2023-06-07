package de.eso.weather.ui.routing.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import de.eso.weather.R

object Routes {

    const val FORECAST = "forecast"

    const val ALERT_LOCATION_ID = "locationId"
    const val ALERT = "alert/{$ALERT_LOCATION_ID}"
    fun toAlerts(locationId: String) = "alert/${locationId}"

    const val MANAGE_LOCATIONS = "manageLocations"

    const val LOCATION_SEARCH = "locationSearch"
    const val LOCATION_SEARCH_RESULT = "locationSearch_result"

    const val THEME_SELECTION = "themeSelection"
}

@Composable
@ReadOnlyComposable
fun ScreenNameFor(route: String?): String {
    return when (route) {
        Routes.FORECAST -> stringResource(id = R.string.forecast_title)
        Routes.ALERT -> stringResource(id = R.string.weather_alerts_title)
        Routes.MANAGE_LOCATIONS -> stringResource(id = R.string.favorite_locations_title)
        Routes.LOCATION_SEARCH -> stringResource(id = R.string.location_add_title)
        Routes.THEME_SELECTION -> stringResource(id = R.string.theme_selection_title)
        else -> ""
    }
}