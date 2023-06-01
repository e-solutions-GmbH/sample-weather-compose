package de.eso.weather.ui.alert

import androidx.compose.ui.test.junit4.createComposeRule
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.platform.Locations
import org.junit.Rule
import org.junit.Test

@Suppress("PrivatePropertyName")
class AlertScreenTest {

    private val ERLANGEN = Locations.knownLocations.first { it.name == "Erlangen" }
    private val ALERT_RADIATION = WeatherAlertTO("RADIATION", ERLANGEN)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun should_show_the_given_location() {
        // GIVEN
        composeTestRule.setContent {
            AlertScreenContent(
                location = ERLANGEN,
                alerts = listOf(AlertListItem(ALERT_RADIATION)),
                isLargeScreen = false
            )
        }

        // THEN
        AlertScreenPage(composeTestRule).locationIsVisible("Erlangen")
    }
}
