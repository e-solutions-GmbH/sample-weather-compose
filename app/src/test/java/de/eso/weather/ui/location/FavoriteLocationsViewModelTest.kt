package de.eso.weather.ui.location

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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class FavoriteLocationsViewModelTest {

    @MockK
    private lateinit var locationService: LocationService

    @MockK
    private lateinit var favoriteLocationsRepository: FavoriteLocationsRepository

    private lateinit var favoriteLocationsViewModel: FavoriteLocationsViewModel

    @Test
    fun `should show the available locations`() {
        // GIVEN
        every { favoriteLocationsRepository.savedLocations } returns just(
            listOf(
                AMMERNDORF,
                ZIRNDORF
            )
        )
        createViewModel()

        // WHEN
        val test = favoriteLocationsViewModel.locations.test()

        // THEN
        test.assertValue(listOf(AMMERNDORF, ZIRNDORF))
    }

    private fun createViewModel() {
        favoriteLocationsViewModel =
            FavoriteLocationsViewModel(favoriteLocationsRepository)
    }
}
