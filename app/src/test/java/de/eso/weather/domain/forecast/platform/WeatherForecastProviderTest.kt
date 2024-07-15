package de.eso.weather.domain.forecast.platform

import de.eso.weather.domain.shared.api.Location
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WeatherForecastProviderTest {

    private val weatherForecastProvider = WeatherForecastProvider()

    @Test
    fun `getCurrentWeather emits forecast for given location`() = runTest {
        // given
        val locationMock = mockk<Location>()

        // when
        val testWeather = weatherForecastProvider.getCurrentWeather(locationMock).first()

        // then
        assertThat(testWeather.location).isEqualTo(locationMock)
        assertThat(testWeather.weather.isNotEmpty()).isTrue()
    }
}
