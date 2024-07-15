package de.eso.weather.domain.forecast.service

import app.cash.turbine.test
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherForecastServiceImplTest {
    private var forecastProvider: ForecastProvider = mockk()

    private lateinit var weatherForecastService: WeatherForecastServiceImpl

    @Test
    fun `getWeather requests immediately current weather and emits its result`() = runTest {
        // given
        weatherForecastService = WeatherForecastServiceImpl(forecastProvider, backgroundScope)
        val locationMock = mockk<Location>()
        val weatherMock = mockk<WeatherTO>()

        every { forecastProvider.getCurrentWeather(locationMock) } returns flowOf(weatherMock)

        // then
        weatherForecastService.getWeather(locationMock).test {
            assertEquals(weatherMock, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `getWeather requests current weather again after 3 seconds`() = runTest {
        weatherForecastService = WeatherForecastServiceImpl(forecastProvider, backgroundScope)
        // given
        val locationMock = mockk<Location>()
        val weatherMock1 = mockk<WeatherTO>()
        val weatherMock2 = mockk<WeatherTO>()

        every { forecastProvider.getCurrentWeather(locationMock) } returnsMany listOf(
            flowOf(weatherMock1),
            flowOf(weatherMock2)
        )

        // then
        weatherForecastService.getWeather(locationMock).test {
            assertEquals(weatherMock1, awaitItem())
            expectNoEvents()
            advanceTimeBy(3_000)
            assertEquals(weatherMock2, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `getWeather multicasts`() = runTest {
        weatherForecastService = WeatherForecastServiceImpl(forecastProvider, backgroundScope)
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns flowOf(mockk<WeatherTO>())

        // when
        val flow = weatherForecastService.getWeather(locationMock)

        flow.first()
        flow.first()
        flow.first()

        // then
        verify(exactly = 1) { forecastProvider.getCurrentWeather(locationMock) }
    }

    @Test
    fun `getWeather multicasts for same location`() = runTest {
        weatherForecastService = WeatherForecastServiceImpl(forecastProvider, backgroundScope)
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns flowOf(mockk<WeatherTO>())

        // when
        weatherForecastService.getWeather(locationMock).first()
        weatherForecastService.getWeather(locationMock).first()
        weatherForecastService.getWeather(locationMock).first()

        // then
        verify(exactly = 1) { forecastProvider.getCurrentWeather(locationMock) }
    }

    @Test
    fun `getWeather keeps requesting weather until last subscription is disposed`() = runTest {
        weatherForecastService = WeatherForecastServiceImpl(forecastProvider, backgroundScope)
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns flowOf(mockk<WeatherTO>())

        // when
        val job1 = launch {
            weatherForecastService.getWeather(locationMock).take(2).collect {}
        }
        job1.join()

        // then
        verify(exactly = 2) { forecastProvider.getCurrentWeather(locationMock) }
    }
}
