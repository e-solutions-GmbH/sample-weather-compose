package de.eso.weather.ui.routing.api

object Routes {

    const val FORECAST = "forecast"

    const val ALERT_LOCATION_ID = "locationId"
    const val ALERT = "alert/{$ALERT_LOCATION_ID}"
    fun toAlerts(locationId: String) = "alert/${locationId}"

    const val MANAGE_LOCATIONS = "manageLocations"

    const val LOCATION_SEARCH = "locationSearch"
    const val LOCATION_SEARCH_RESULT = "locationSearch_result"
}
