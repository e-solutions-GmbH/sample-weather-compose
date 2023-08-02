package de.eso.weather.ui.forecast

import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import org.junit.Rule
import org.junit.Test

/**
 * Showcase how to test compose screens in isolation, i.e. without any domain logic and without Koin
 */
class ForecastScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val weatherScreenPage = WeatherScreenPage(
        InstrumentationRegistry.getInstrumentation().targetContext,
        composeTestRule
    )

    @Test
    fun should_show_the_current_weather() {
        // GIVEN
        showScreen()

        // THEN
        weatherScreenPage
            .weatherIsVisible(GOOD_WEATHER)
    }

    private fun showScreen() {
        composeTestRule.setContent {
            ForecastScreenContent(
                viewState = ForecastViewState(ERLANGEN, WeatherTO(GOOD_WEATHER, ERLANGEN)),
                savedLocationsViewState = listOf(ForecastViewState(ERLANGEN, WeatherTO(GOOD_WEATHER, ERLANGEN))),
                onGoToWeatherAlertsClicked = { },
                onAddLocationClicked = { }
            )
        }
    }

    private companion object {
        private val ERLANGEN = Location(name = "Erlangen")
        private const val GOOD_WEATHER = "Good Weather"
    }
}
