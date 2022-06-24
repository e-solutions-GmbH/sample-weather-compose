package de.eso.weather.ui.forecast

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import de.eso.weather.ui.WeatherActivity
import de.eso.weather.ui.alert.AlertFragmentPage
import de.eso.weather.ui.location.favorites.FavoriteLocationsPage
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

/**
 * Showcase how to test compose screens using the real [WeatherActivity]/[WeatherPrimaryDisplayActivity]
 */
@Suppress("HasPlatformType")
class WeatherIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<WeatherActivity>()

    private val weatherFragmentPage = WeatherFragmentPage(InstrumentationRegistry.getInstrumentation().targetContext, composeTestRule)
    private val favoriteLocationsPage = FavoriteLocationsPage(InstrumentationRegistry.getInstrumentation().targetContext, composeTestRule)
    private val alertFragmentPage = AlertFragmentPage(composeTestRule)

    @Test
    fun should_navigate_to_the_alerts_screen_when_clicking_on_alerts() {
        // WHEN
        weatherFragmentPage.clickGoToAlertsForButton("Erlangen")

        // THEN
        alertFragmentPage.isVisible()
    }

    @Test
    fun should_show_the_favorite_locations_when_clicking_the_locations_button() {
        // WHEN
        weatherFragmentPage.clickManageLocationsButton()

        // THEN
        favoriteLocationsPage.isVisible()
    }
}
