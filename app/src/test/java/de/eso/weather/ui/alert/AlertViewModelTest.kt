package de.eso.weather.ui.alert

import de.eso.weather.InstantTaskExecutorExtension
import de.eso.weather.domain.alert.api.WeatherAlertService
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.location.api.LocationService
import de.eso.weather.domain.shared.api.Location
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable.just
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class AlertViewModelTest {

    @MockK
    private lateinit var weatherAlertService: WeatherAlertService

    @MockK
    private lateinit var locationService: LocationService

    @Test
    fun `Should emit the weather alerts from its weather alert provider`() {
        // GIVEN
        every { locationService.getLocation(ERLANGEN.id) } returns Maybe.just(ERLANGEN)
        every { weatherAlertService.getWeatherAlerts(any()) } returns just(listOf(ALERT_TO))

        val alertViewModel = createViewModel()

        // WHEN
        val testObserver = alertViewModel.weatherAlerts.test()

        // THEN
        testObserver.assertValue(listOf(AlertListItem(ALERT_TO)))
    }

    private fun createViewModel() =
        AlertViewModel(weatherAlertService, locationService, ERLANGEN.id)

    private companion object {
        private val ERLANGEN = Location(name = "Erlangen", id = "1-2-3-4-5")
        private const val NO_ALERT = "No alert"
        private val ALERT_TO = WeatherAlertTO(NO_ALERT, ERLANGEN)
    }
}
