package de.eso.weather.ui.location.search

import de.eso.weather.CoroutineMainDispatcherExtension
import de.eso.weather.InstantTaskExecutorExtension
import de.eso.weather.Locations.AMMERNDORF
import de.eso.weather.Locations.ERLANGEN
import de.eso.weather.Locations.ZIRNDORF
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext


@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class, CoroutineMainDispatcherExtension::class)
class LocationSearchViewModelTest {
    @MockK
    private lateinit var locationService: LocationService

    @RelaxedMockK
    private lateinit var favoriteLocationsRepository: FavoriteLocationsRepository

    private lateinit var locationSearchViewModel: LocationSearchViewModel

    @BeforeEach
    fun setup() {
        setupAvailableLocations()
    }

    @Test
    fun `should emit search results for empty search after initialization`() = runTest {
        // GIVEN
        every { locationService.queryLocations("") } returns flowOf(listOf(AMMERNDORF, ERLANGEN))

        // WHEN
        createViewModel()

        // THEN
        assertThat(locationSearchViewModel.locations.value).containsExactly(AMMERNDORF, ERLANGEN)
    }

    @Test
    fun `should finish with the selected location when selecting a location`() = runTest {
        // GIVEN
        every { locationService.queryLocations(AMMERNDORF.name) } returns flowOf(listOf(AMMERNDORF))

        // WHEN
        createViewModel()
        locationSearchViewModel.onLocationSelected(AMMERNDORF)

        // THEN
        assertThat(locationSearchViewModel.finishScreen.value).isNotNull()
    }

    private fun createViewModel() {
        locationSearchViewModel = LocationSearchViewModel(locationService, favoriteLocationsRepository)
    }

    private fun setupAvailableLocations() {
        every { locationService.queryLocations("") } returns flowOf(listOf(AMMERNDORF, ZIRNDORF))
    }
}
