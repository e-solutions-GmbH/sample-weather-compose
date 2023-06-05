package de.eso.weather.ui.location.search

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.eso.weather.ui.onTextInButton

class LocationSearchPage(private val composeTestRule: ComposeTestRule) {

    private val LOCATION_SEARCH_SCREEN = composeTestRule.onNodeWithText("Available Locations")

    fun isVisible() = also {
        LOCATION_SEARCH_SCREEN.assertIsDisplayed()
    }
}
