package de.eso.weather.domain.alert.platform

import de.eso.weather.domain.alert.service.AlertReceiver

class WeatherAlertConnector(provider: AlertProvider, receiver: AlertReceiver) {
    init {
        connectProviderAndReceiver(provider, receiver)
    }

    private fun connectProviderAndReceiver(provider: AlertProvider, receiver: AlertReceiver) {
        @Suppress("IgnoredReturnValue") // Ignored on purpose because this should act as a hot stream
        provider.alerts().subscribe(receiver::updateAlerts)
    }
}
