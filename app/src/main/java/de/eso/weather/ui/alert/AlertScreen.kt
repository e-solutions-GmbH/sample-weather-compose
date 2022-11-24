package de.eso.weather.ui.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import de.eso.weather.R
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.Tile

@Composable
fun AlertScreen(viewModel: AlertViewModel) {
    val location by viewModel.location.subscribeAsState(null)
    val alerts by viewModel.weatherAlerts.subscribeAsState(initial = emptyList())

    AlertScreenContent(
        location = location,
        alerts = alerts,
        isLargeScreen = WeatherTheme.isLargeScreen()
    )
}

@Composable
fun AlertScreenContent(
    location: Location?,
    alerts: List<AlertListItem>,
    isLargeScreen: Boolean = false
) {
    val text = if (location != null) {
        stringResource(id = R.string.weather_alerts_headline, location.name)
    } else {
        stringResource(R.string.weather_alerts_unknown_location)
    }

    val hasAlerts = alerts.any { alertListItem ->
        val alertName = alertListItem.alert.alert

        alertName == "Ozone" || alertName == "Radiation" || alertName == "Pollen"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            style = WeatherTheme.typography.h6
        )

        if (hasAlerts) {
            Alerts(
                alerts = alerts,
                modifier = Modifier.layoutId("alertList"),
                isLargeScreen = isLargeScreen
            )
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No warning currently, the weather is fine!")
            }
        }
    }
}

@Composable
fun Alerts(
    alerts: List<AlertListItem>,
    isLargeScreen: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isLargeScreen) {
        LazyRow(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            content = {
                items(alerts) {
                    AlertTile(
                        alertListItem = it,
                        modifier = Modifier.size(Dimensions.TileSizeLarge)
                    )
                }
            }
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = Dimensions.ContainerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                items(alerts) {
                    AlertTile(
                        alertListItem = it,
                        modifier = Modifier
                            .size(Dimensions.TileSize)
                            .padding(bottom = Dimensions.ContainerPadding)
                    )
                }
            }
        )
    }
}

@Composable
fun AlertTile(
    alertListItem: AlertListItem,
    modifier: Modifier = Modifier
) {
    val alertIcon = when (alertListItem.alert.alert) {
        "Radiation" -> Pair(R.drawable.ic_alert_radiation, EsoColors.Green)
        "Pollen" -> Pair(R.drawable.ic_alert_pollen, EsoColors.Pink)
        "Ozone" -> Pair(R.drawable.ic_alert_ozone, EsoColors.Violet)
        else -> null
    }

    Tile(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        alertIcon?.let {
            Icon(
                painter = painterResource(id = alertIcon.first),
                tint = alertIcon.second,
                contentDescription = alertListItem.alert.alert,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = Dimensions.IconPadding)
            )
        }
        Text(
            text = alertListItem.alert.alert,
            style = WeatherTheme.typography.subtitle1,
            modifier = Modifier
        )
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun AlertScreenContentPreviewAutomotive() {
    AlertScreenContentPreview(isLargeScreen = true)
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun AlertScreenContentNoAlertsPreviewAutomotive() {
    AlertScreenContentNoAlertsPreview(isLargeScreen = true)
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun AlertScreenContentPreviewDefault() {
    AlertScreenContentPreview()
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun AlertScreenContentNoAlertsPreviewDefault() {
    AlertScreenContentNoAlertsPreview()
}

@Composable
fun AlertScreenContentPreview(isLargeScreen: Boolean = false) {
    val location = Location("Erlangen")

    WeatherTheme {
        AlertScreenContent(
            location = location,
            alerts = listOf(
                AlertListItem(WeatherAlertTO("Ozone", location)),
                AlertListItem(WeatherAlertTO("Radiation", location)),
                AlertListItem(WeatherAlertTO("Pollen", location))
            ),
            isLargeScreen = isLargeScreen
        )
    }
}

@Composable
fun AlertScreenContentNoAlertsPreview(isLargeScreen: Boolean = false) {
    val location = Location("Erlangen")

    WeatherTheme {
        AlertScreenContent(
            location = location,
            alerts = listOf(),
            isLargeScreen = isLargeScreen
        )
    }
}
