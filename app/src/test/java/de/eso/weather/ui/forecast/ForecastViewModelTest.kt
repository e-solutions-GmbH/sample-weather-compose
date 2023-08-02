package de.eso.weather.ui.forecast

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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class ForecastViewModelTest {

    private val testScheduler = Schedulers.trampoline()

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
        every { weatherForecastService.getWeather(any()) } returns just(WEATHER_IN_ERLANGEN)

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
        every { locationService.availableLocations } returns just(listOf(ERLANGEN))
    }

    private fun setupActiveLocation() {
        every { favoriteLocationsRepository.activeLocation } returns just(Optional.of(ERLANGEN))
    }

    private fun setupSavedLocations() {
        every { favoriteLocationsRepository.savedLocations } returns just(Locations.knownLocations)
    }

    private fun createViewModel() {
        forecastViewModel =
            ForecastViewModel(weatherForecastService, favoriteLocationsRepository, testScheduler)
    }

    private companion object {
        private const val GOOD_WEATHER = "Good Weather"
        private val WEATHER_IN_ERLANGEN = WeatherTO(GOOD_WEATHER, ERLANGEN)
    }
}
