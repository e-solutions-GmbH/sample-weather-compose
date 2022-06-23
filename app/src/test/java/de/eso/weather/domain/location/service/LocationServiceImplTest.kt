package de.eso.weather.domain.location.service

import de.eso.weather.Locations.AMMERNDORF
import de.eso.weather.Locations.ZIRNDORF
import de.eso.weather.domain.shared.api.Location
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LocationServiceImplTest {

    private val locationService = LocationServiceImpl()

    @Test
    fun `no locations are available by default`() {
        // when
        val testObserver = locationService.availableLocations.test()

        // then
        testObserver.assertNoValues().assertNotComplete()
    }

    @Test
    fun `availableLocations contains locations received by updateAvailableLocations`() {
        // given
        val locationMocks = listOf(AMMERNDORF, ZIRNDORF)
        locationService.updateAvailableLocations(locationMocks)

        // when
        val testObserver = locationService.availableLocations.test()

        // then
        testObserver.assertValue(locationMocks).assertNotComplete()
    }

    @Test
    fun `getLocation completes when location with given id is not available`() {
        // given
        val location1 = Location("location1", "1-1-1-1-1")
        val location2 = Location("location2", "2-2-2-2-2")
        val location3 = Location("location3", "3-3-3-3-3")

        // > send all locations and then remove second location
        locationService.updateAvailableLocations(listOf(location1, location2, location3))
        locationService.updateAvailableLocations(listOf(location1, location3))

        // when
        val testObserver = locationService.getLocation(location2.id).test()

        // then
        testObserver.assertNoValues().assertComplete()
    }

    @Test
    fun `getLocation emits location when it is available`() {
        // given
        val location1 = Location("location1", "1-1-1-1-1")
        val location2 = Location("location2", "2-2-2-2-2")

        locationService.updateAvailableLocations(listOf(location1, location2))

        // when
        val testObserver = locationService.getLocation(location2.id).test()

        // then
        testObserver.assertValue(location2).assertComplete()
    }

    @ParameterizedTest
    @ValueSource(strings = ["location", "cat", "LOCATION", " cation "])
    fun `queryLocations searches locations correctly`(searchString: String) {
        // given
        val location = Location("location", "1-1-1-1-1")

        locationService.updateAvailableLocations(listOf(location))

        // when
        val testObserver = locationService.queryLocations(searchString).test()

        // then
        testObserver.assertValue(listOf(location)).assertComplete()
    }

    @Test
    fun `queryLocations emits empty result list when no matching location is found`() {
        // given
        val location = Location("Location", "1-1-1-1-1")

        locationService.updateAvailableLocations(listOf(location))

        // when
        val testObserver = locationService.queryLocations("frankfurt").test()

        // then
        testObserver.assertValue(emptyList()).assertComplete()
    }

    @Test
    fun `queryLocations emits empty result list when no locations are available`() {
        // given
        locationService.updateAvailableLocations(emptyList())

        // when
        val testObserver = locationService.queryLocations("location").test()

        // then
        testObserver.assertValue(emptyList()).assertComplete()
    }
}
