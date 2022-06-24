package de.eso.weather.ui.location.favorites

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.eso.weather.ui.onTextInButton

class FavoriteLocationsPage(private val context: Context, private val composeTestRule: ComposeTestRule) {

    private val ADD_LOCATION_BUTTON = composeTestRule.onTextInButton("+")

    fun isVisible() = also {
        ADD_LOCATION_BUTTON.assertIsDisplayed()
    }

    fun locationIsVisible(vararg locations: String) = also {
        locations.forEach {
            composeTestRule.onNodeWithText(it).assertIsDisplayed()
        }
    }

    fun clickAddLocationButton() = also {
        ADD_LOCATION_BUTTON.performClick()
    }
}
