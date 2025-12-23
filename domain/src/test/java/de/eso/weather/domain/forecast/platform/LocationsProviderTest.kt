package de.eso.weather.domain.forecast.platform

import de.eso.weather.domain.location.platform.LocationsProvider
import de.eso.weather.domain.location.service.LocationsReceiver
import de.eso.weather.domain.shared.platform.Locations
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class LocationsProviderTest {

    @Test
    fun `all known locations are immediately sent`() {
        // given
        val locationsReceiverMock = mockk<LocationsReceiver>(relaxed = true)

        // when
        LocationsProvider(locationsReceiverMock)

        // then
        verify { locationsReceiverMock.updateAvailableLocations(Locations.knownLocations) }
    }
}
