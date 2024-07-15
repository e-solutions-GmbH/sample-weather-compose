package de.eso.weather.domain.location.service

import app.cash.turbine.test
import de.eso.weather.Locations.AMMERNDORF
import de.eso.weather.Locations.ZIRNDORF
import de.eso.weather.domain.shared.api.Location
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LocationServiceImplTest {

    private val locationService = LocationServiceImpl()

    @Test
    fun `no locations are available by default`() = runTest {
        // when
        locationService.availableLocations.test {
            // then
            expectNoEvents()
        }
    }

    @Test
    fun `availableLocations contains locations received by updateAvailableLocations`() = runTest {
        // given
        val locationMocks = listOf(AMMERNDORF, ZIRNDORF)

        // when
        locationService.updateAvailableLocations(locationMocks)

        // then
        locationService.availableLocations.test {
            assertEquals(locationMocks, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `getLocation completes when location with given id is not available`() = runTest {
        // given
        val location1 = Location("location1", "1-1-1-1-1")
        val location2 = Location("location2", "2-2-2-2-2")
        val location3 = Location("location3", "3-3-3-3-3")

        // when
        // > send all locations and then remove second location
        locationService.updateAvailableLocations(listOf(location1, location2, location3))
        locationService.updateAvailableLocations(listOf(location1, location3))

        // then
        locationService.getLocation(location2.id).test {
            awaitComplete()
        }
    }

    @Test
    fun `getLocation emits location when it is available`() = runTest {
        // given
        val location1 = Location("location1", "1-1-1-1-1")
        val location2 = Location("location2", "2-2-2-2-2")

        // when


        // then
        locationService.getLocation(location2.id).test {
            locationService.updateAvailableLocations(listOf(location1, location2))
            assertEquals(location2, awaitItem())
            awaitComplete()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["location", "cat", "LOCATION", " cation "])
    fun `queryLocations searches locations correctly`(searchString: String) = runTest {
        // given
        val location = Location("location", "1-1-1-1-1")

        // when
        locationService.updateAvailableLocations(listOf(location))

        // then
        locationService.queryLocations(searchString).test {
            assertEquals(listOf(location), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `queryLocations emits empty result list when no matching location is found`() = runTest {
        // given
        val location = Location("Location", "1-1-1-1-1")

        // when
        locationService.updateAvailableLocations(listOf(location))

        // then
        locationService.queryLocations("frankfurt").test {
            assertEquals(emptyList<Location>(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `queryLocations emits empty result list when no locations are available`() = runTest {
        // given
        locationService.updateAvailableLocations(emptyList())

        // then
        locationService.queryLocations("location").test {
            assertEquals(emptyList<Location>(), awaitItem())
            awaitComplete()
        }
    }
}
