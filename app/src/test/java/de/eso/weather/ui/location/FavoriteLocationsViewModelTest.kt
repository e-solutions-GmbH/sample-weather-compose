package de.eso.weather.ui.location

import app.cash.turbine.test
import de.eso.weather.InstantTaskExecutorExtension
import de.eso.weather.Locations.AMMERNDORF
import de.eso.weather.Locations.ZIRNDORF
import de.eso.weather.domain.location.api.FavoriteLocationsRepository
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.ui.location.favorites.FavoriteLocationsViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.reactivex.rxjava3.core.Observable.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class FavoriteLocationsViewModelTest {
    @MockK
    private lateinit var favoriteLocationsRepository: FavoriteLocationsRepository

    private lateinit var favoriteLocationsViewModel: FavoriteLocationsViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `should show the available locations`() = runTest {
        // GIVEN
        every { favoriteLocationsRepository.savedLocations } returns flowOf(
            listOf(
                AMMERNDORF,
                ZIRNDORF
            )
        )

        // WHEN
        createViewModel()


        // THEN
        favoriteLocationsViewModel.locations.test {
            assertEquals(listOf(AMMERNDORF, ZIRNDORF), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() {
        favoriteLocationsViewModel =
            FavoriteLocationsViewModel(favoriteLocationsRepository)
    }
}
