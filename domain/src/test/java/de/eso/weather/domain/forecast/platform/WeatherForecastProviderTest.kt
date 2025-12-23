package de.eso.weather.domain.forecast.platform

import de.eso.weather.domain.shared.api.Location
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class WeatherForecastProviderTest {

    private val weatherForecastProvider = WeatherForecastProvider()

    @Test
    fun `getCurrentWeather emits forecast for given location`() {
        // given
        val locationMock = mockk<Location>()

        // when
        val testObserver = weatherForecastProvider.getCurrentWeather(locationMock).test()

        // then
        testObserver.assertValue { it.location == locationMock && it.weather.isNotEmpty() }
    }
}
