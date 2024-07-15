package de.eso.weather.ui.forecast

import de.eso.weather.CoroutineMainDispatcherExtension
import de.eso.weather.InstantTaskExecutorExtension
import de.eso.weather.Locations.ERLANGEN
import de.eso.weather.domain.forecast.api.WeatherForecastService
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.platform.Locations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class, CoroutineMainDispatcherExtension::class)
class ForecastViewModelTest {
    @MockK
    private lateinit var weatherForecastService: WeatherForecastService

    @MockK
    private lateinit var locationService: LocationService

    @MockK
    private lateinit var favoriteLocationsRepository: FavoriteLocationsRepository

    private lateinit var forecastViewModel: ForecastViewModel

    @BeforeEach
    fun setup() {
        setupAvailableLocations()
        setupSavedLocations()
    }

    @Test
    fun `Should emit the weather from its weather provider`() {
        // GIVEN
        setupActiveLocation()
        every { weatherForecastService.getWeather(any()) } returns flowOf(WEATHER_IN_ERLANGEN)

        // WHEN
        createViewModel()

        // THEN
        assertThat(forecastViewModel.forecastViewState).isEqualTo(
            ForecastViewState(
                ERLANGEN,
                WEATHER_IN_ERLANGEN
            )
        )
    }

    private fun setupAvailableLocations() {
        every { locationService.availableLocations } returns flowOf(listOf(ERLANGEN))
    }

    private fun setupActiveLocation() {
        every { favoriteLocationsRepository.activeLocation } returns flowOf(Optional.of(ERLANGEN))
    }

    private fun setupSavedLocations() {
        every { favoriteLocationsRepository.savedLocations } returns flowOf(Locations.knownLocations)
    }

    private fun createViewModel() {
        forecastViewModel =
            ForecastViewModel(weatherForecastService, favoriteLocationsRepository)
    }

    private companion object {
        private const val GOOD_WEATHER = "Good Weather"
        private val WEATHER_IN_ERLANGEN = WeatherTO(GOOD_WEATHER, ERLANGEN)
    }
}
