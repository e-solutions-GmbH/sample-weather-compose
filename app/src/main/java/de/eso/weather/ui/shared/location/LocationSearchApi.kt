package de.eso.weather.ui.shared.location

import android.content.Context
import android.content.Intent
import de.eso.weather.ui.location.search.LocationSearchActivity

object LocationSearchApi {
    const val RESULT_KEY_LOCATION = "Location"

    fun getLocationActivityIntent(context: Context) = Intent(context, LocationSearchActivity::class.java)
}
