package de.eso.weather.ui.location.search

import de.eso.weather.InstantTaskExecutorExtension
import de.eso.weather.Locations.AMMERNDORF
import de.eso.weather.Locations.ERLANGEN
import de.eso.weather.Locations.ZIRNDORF
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.test
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.reactivex.rxjava3.core.Single.just
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class LocationSearchViewModelTest {

    @MockK
    private lateinit var locationService: LocationService

    private lateinit var locationSearchViewModel: LocationSearchViewModel

    @BeforeEach
    fun setup() {
        setupAvailableLocations()
    }

    @Test
    fun `should emit search results for empty search after initialization`() {
        // GIVEN
        every { locationService.queryLocations("") } returns just(listOf(AMMERNDORF, ERLANGEN))

        // WHEN
        createViewModel()

        // THEN
        assertThat(locationSearchViewModel.locationsResult.value).containsExactly(AMMERNDORF, ERLANGEN)
    }

    @Test
    fun `should finish with the selected location when selecting a location`() {
        // GIVEN
        every { locationService.queryLocations(AMMERNDORF.name) } returns just(listOf(AMMERNDORF))

        createViewModel()
        val testObserver = locationSearchViewModel.finishWithResult.test()

        // WHEN
        locationSearchViewModel.onLocationSelected(AMMERNDORF)

        // THEN
        testObserver.assertValue { it.peekContent() == AMMERNDORF }
    }

    private fun createViewModel() {
        locationSearchViewModel = LocationSearchViewModel(locationService)
    }

    private fun setupAvailableLocations() {
        every { locationService.queryLocations("") } returns just(listOf(AMMERNDORF, ZIRNDORF))
    }
}
