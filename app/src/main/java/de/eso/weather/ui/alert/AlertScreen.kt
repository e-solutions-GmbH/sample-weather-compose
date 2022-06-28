package de.eso.weather.ui.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import de.eso.weather.R
import de.eso.weather.domain.alert.api.WeatherAlertTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun AlertScreen(viewModel: AlertViewModel) {
    val location by viewModel.location.subscribeAsState(null)
    val alerts by viewModel.weatherAlerts.subscribeAsState(initial = emptyList())

    AlertScreenContent(
        location,
        alerts
    )
}

@Composable
fun AlertScreenContent(
    location: Location?,
    alerts: List<AlertListItem>
) {
    BoxWithConstraints {
        val constraints = ConstraintSet {
            val weatherAlertsTitle = createRefFor("weatherAlertsTitle")
            val alertList = createRefFor("alertList")

            constrain(weatherAlertsTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(alertList) {
                top.linkTo(weatherAlertsTitle.bottom, margin = Dimensions.PADDING_MEDIUM)
                bottom.linkTo(parent.bottom, margin = Dimensions.PADDING_MEDIUM)
                start.linkTo(parent.start)
                height = Dimension.fillToConstraints
            }
        }

        ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
            val text = if (location != null) {
                stringResource(id = R.string.weather_alerts_headline, location.name)
            } else {
                stringResource(R.string.weather_alerts_unknown_location)
            }
            Text(text = text, modifier = Modifier.layoutId("weatherAlertsTitle"))
            Alerts(alerts, modifier = Modifier.layoutId("alertList"))
        }
    }
}

@Composable
fun Alerts(alerts: List<AlertListItem>, modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        modifier = modifier,
        content = {
            items(alerts) { alertListItem ->
                Text(text = alertListItem.alert.alert)
            }
        })
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Preview(device = Devices.PIXEL_4)
@Composable
fun AlertScreenContentPreview() {
    val location = Location("Erlangen")

    WeatherTheme {
        AlertScreenContent(
            location = location,
            alerts = listOf(
                AlertListItem(WeatherAlertTO("Ozone", location)),
                AlertListItem(WeatherAlertTO("Radiation", location)),
                AlertListItem(WeatherAlertTO("Snow", location))
            )
        )
    }
}
