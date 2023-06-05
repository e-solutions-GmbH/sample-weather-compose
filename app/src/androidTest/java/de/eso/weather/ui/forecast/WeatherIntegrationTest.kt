package de.eso.weather.ui.forecast

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import de.eso.weather.ui.WeatherActivity
import de.eso.weather.ui.alert.AlertScreenPage
import de.eso.weather.ui.location.favorites.FavoriteLocationsPage
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

/**
 * Showcase how to test compose screens using the real [WeatherActivity]
 */
@Suppress("HasPlatformType")
class WeatherIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<WeatherActivity>()

    private val weatherScreenPage = WeatherScreenPage(InstrumentationRegistry.getInstrumentation().targetContext, composeTestRule)
    private val favoriteLocationsPage = FavoriteLocationsPage(InstrumentationRegistry.getInstrumentation().targetContext, composeTestRule)
    private val alertScreenPage = AlertScreenPage(composeTestRule)

    @Test
    fun should_navigate_to_the_alerts_screen_when_clicking_on_alerts() {
        // WHEN
        weatherScreenPage.clickGoToAlertsForButton("Erlangen")

        // THEN
        alertScreenPage.isVisible()
    }

    @Test
    @Ignore("Does currently not click on the manage locations button")
    fun should_show_the_favorite_locations_when_clicking_the_locations_button() {
        // WHEN
        weatherScreenPage.clickManageLocationsButton()

        // THEN
        favoriteLocationsPage.isVisible()
    }
}
