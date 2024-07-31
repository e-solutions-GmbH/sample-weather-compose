package de.eso.weather.ui.forecast

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.forecast.service.ForecastProvider
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.WeatherActivity
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * Showcase how to test compose screens with actual ViewModels, domain logic and Koin,
 * but without the real [WeatherActivity].
 */
class ForecastScreenMediumTest: KoinTest {

    private class DummyForecastProvider : ForecastProvider {
        override fun getCurrentWeather(location: Location) =
            flowOf(WeatherTO(GOOD_WEATHER, ERLANGEN))
    }

    private val forecastViewModel: ForecastViewModel by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)

    private val weatherScreenPage = WeatherScreenPage(
        InstrumentationRegistry.getInstrumentation().targetContext,
        composeTestRule
    )

    @Before
    fun setup() {
        loadKoinModules(
            listOf(
                module(override = true) {
                    single<ForecastProvider> {
                        DummyForecastProvider()
                    }
                }
            )
        )
    }

    // TODO: Make this work on GitHub Actions (timing issue?)
    @Ignore("Flaky on CI")
    @Test
    fun should_show_the_current_weather() {
        // GIVEN
        showScreen()

        // THEN
        weatherScreenPage
            .weatherIsVisible(ERLANGEN, GOOD_WEATHER)
    }

    private fun showScreen() {
        composeTestRule.setContent {
            ForecastScreen(navController, forecastViewModel)
        }
    }

    private companion object {
        private val ERLANGEN = Location(name = "Erlangen")
        private const val GOOD_WEATHER = "Good Weather"
    }
}
