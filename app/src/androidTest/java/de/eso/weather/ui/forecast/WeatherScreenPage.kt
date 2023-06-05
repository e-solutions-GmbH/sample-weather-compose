package de.eso.weather.ui.forecast

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.eso.weather.R
import de.eso.weather.ui.onTextInButton

@Suppress("HasPlatformType")
class WeatherScreenPage(private val context: Context, private val composeTestRule: ComposeTestRule) {

    private val MANAGE_LOCATIONS_BUTTON = composeTestRule.onNodeWithText(context.resources.getString(R.string.manage_locations_button))

    fun weatherIsVisible(weather: String) = also {
        composeTestRule.onNode(hasText(weather)).assertIsDisplayed()
    }

    fun clickGoToAlertsForButton(location: String) = also {
        onWeatherAlertsButton(location).performClick()
    }

    fun clickManageLocationsButton() = also {
        MANAGE_LOCATIONS_BUTTON.performClick()
    }

    private fun onWeatherAlertsButton(location: String) =
        composeTestRule.onNodeWithText(context.resources.getString(R.string.weather_alerts_headline, location))
}
