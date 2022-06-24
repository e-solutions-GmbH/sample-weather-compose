package de.eso.weather.ui.routing.api

import android.content.ComponentName
import android.content.Context
import android.content.Intent

object LocationSearch {
    fun getLocationActivityIntent(context: Context) =
        Intent().apply { component = ComponentName(context, "de.eso.weather.ui.location.search.LocationSearchActivity") }
}
