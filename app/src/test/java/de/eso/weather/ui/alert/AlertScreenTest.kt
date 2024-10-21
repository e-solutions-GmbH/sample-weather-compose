package de.eso.weather.ui.alert

import androidx.compose.material.Text
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.platform.Locations
import de.eso.weather.ui.WeatherActivity
import de.eso.weather.ui.shared.compose.WeatherTheme
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("PrivatePropertyName")
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME)
class AlertScreenTest {

    private val ERLANGEN = Locations.knownLocations.first { it.name == "Erlangen" }
    private val ALERT_RADIATION = WeatherAlertTO("RADIATION", ERLANGEN)

    @get:Rule val composeTestRule = createComposeRule()


    @Test
    fun should_show_the_given_location() {
        // GIVEN
        composeTestRule.setContent {
            /*AlertScreenContent(
                location = ERLANGEN,
                alerts = listOf(AlertListItem(ALERT_RADIATION)),
                isLargeScreen = false
            )*/
            Text(
                text = "text",
                style = WeatherTheme.typography.h6
            )
        }

        // THEN
        AlertScreenPage(composeTestRule).locationIsVisible("Erlangen")
    }
}
