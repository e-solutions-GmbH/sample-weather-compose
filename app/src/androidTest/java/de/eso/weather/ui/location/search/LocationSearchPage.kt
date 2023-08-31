package de.eso.weather.ui.location.search

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import de.eso.weather.R

class LocationSearchPage(context: Context, composeTestRule: ComposeTestRule) {

    private val LOCATION_SEARCH_SCREEN = composeTestRule.onNodeWithText(context.resources.getString(R.string.location_add_title))

    fun isVisible() = also {
        LOCATION_SEARCH_SCREEN.assertIsDisplayed()
    }
}
