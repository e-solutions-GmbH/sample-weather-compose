package de.eso.weather.domain.alert.service

import de.eso.weather.domain.alert.api.WeatherAlertTO

interface AlertReceiver {

    fun updateAlerts(alerts: List<WeatherAlertTO>)
}
