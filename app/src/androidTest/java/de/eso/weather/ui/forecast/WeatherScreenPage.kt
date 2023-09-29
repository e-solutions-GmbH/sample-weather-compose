package de.eso.weather.ui.forecast

import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.eso.weather.R
import de.eso.weather.domain.shared.api.Location

@Suppress("HasPlatformType")
class WeatherScreenPage(private val context: Context, private val composeTestRule: ComposeTestRule) {

    private val FAVORITE_LOCATIONS_BUTTON = composeTestRule.onNodeWithText(context.resources.getString(R.string.favorite_locations_title_short))

    @OptIn(ExperimentalTestApi::class)
    fun weatherIsVisible(location: Location, weather: String) = also {
        // Wait until corresponding location tile is visible
        composeTestRule.waitUntilExactlyOneExists(hasText(location.name), timeoutMillis = 1000L)

        composeTestRule.onNode(hasText(weather)).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickGoToAlertsForButton(location: String) = also {
        // Wait until corresponding location tile is visible
        composeTestRule.waitUntilExactlyOneExists(hasText(location), timeoutMillis = 1000L)

        onWeatherAlertsButton(location).performClick()
    }

    fun clickFavoriteLocationsButton() = also {
        FAVORITE_LOCATIONS_BUTTON.performClick()
    }

    private fun onWeatherAlertsButton(location: String) =
        composeTestRule.onNodeWithText(context.resources.getString(R.string.weather_alerts_headline, location))
}
