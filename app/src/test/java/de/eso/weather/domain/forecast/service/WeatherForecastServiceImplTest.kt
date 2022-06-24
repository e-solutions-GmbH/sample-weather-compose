package de.eso.weather.domain.forecast.service

import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class WeatherForecastServiceImplTest {

    private var forecastProvider: ForecastProvider = mockk()
    private val testScheduler = TestScheduler()

    private lateinit var weatherForecastService: WeatherForecastServiceImpl

    @BeforeEach
    fun init() {
        weatherForecastService = WeatherForecastServiceImpl(testScheduler, forecastProvider)
    }

    @Test
    fun `getWeather requests immediately current weather and emits its result`() {
        // given
        val locationMock = mockk<Location>()
        val weatherMock = mockk<WeatherTO>()

        every { forecastProvider.getCurrentWeather(locationMock) } returns Single.just(weatherMock)

        // when
        val testObserver = weatherForecastService.getWeather(locationMock).test()
        testScheduler.triggerActions()

        // then
        testObserver.assertValue(weatherMock).assertNotComplete()
    }

    @Test
    fun `getWeather requests current weather again after 3 seconds`() {
        // given
        val locationMock = mockk<Location>()
        val weatherMock1 = mockk<WeatherTO>()
        val weatherMock2 = mockk<WeatherTO>()

        every { forecastProvider.getCurrentWeather(locationMock) } returnsMany listOf(
            Single.just(weatherMock1),
            Single.just(weatherMock2)
        )

        // when
        val testObserver = weatherForecastService.getWeather(locationMock).test()
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        // then
        testObserver.assertValues(weatherMock1, weatherMock2).assertNotComplete()
    }

    @Test
    fun `getWeather multicasts`() {
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns Single.never()

        // when
        val weatherObserver = weatherForecastService.getWeather(locationMock)

        weatherObserver.subscribe()
        weatherObserver.subscribe()
        weatherObserver.subscribe()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        // then
        verify(exactly = 1) { forecastProvider.getCurrentWeather(locationMock) }
    }

    @Test
    fun `getWeather multicasts for same location`() {
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns Single.never()

        // when
        weatherForecastService.getWeather(locationMock).subscribe()
        weatherForecastService.getWeather(locationMock).subscribe()
        weatherForecastService.getWeather(locationMock).subscribe()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        // then
        verify(exactly = 1) { forecastProvider.getCurrentWeather(locationMock) }
    }

    @Test
    fun `getWeather keeps requesting weather until last subscription is disposed`() {
        // given
        val locationMock = mockk<Location>()
        every { forecastProvider.getCurrentWeather(locationMock) } returns Single.never()

        // when
        val disposable1 = weatherForecastService.getWeather(locationMock).test()
        val disposable2 = weatherForecastService.getWeather(locationMock).test()

        disposable1.dispose()

        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        disposable2.dispose()

        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        // then
        verify(exactly = 2) { forecastProvider.getCurrentWeather(locationMock) }
    }
}
