package de.eso.weather.ui.forecast

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import de.eso.weather.R
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.shared.compose.Dimensions
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ForecastScreen(navController: NavController, viewModel: ForecastViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    ForecastScreenContent(
        viewModel.forecastViewState,
        snackbarHostState,
        onGoToWeatherAlertsClicked = {
            navController.navigate(
                Routes.toAlerts(
                    checkNotNull(viewModel.forecastViewState.activeLocation?.id)
                )
            )
        },
        viewModel::onShowDummySnackbarClicked,
        onManageLocationsClicked = {
            navController.navigate(Routes.MANAGE_LOCATIONS)
        },
        viewModel::onSimulateLocationGoneButton
    )

    val showDummySnackbarCommand by viewModel.showDummySnackbar.observeAsState()

    val coroutineScope = rememberCoroutineScope()
    if (showDummySnackbarCommand?.hasBeenHandled == false) {
        showDummySnackbarCommand?.hasBeenHandled = true
        val snackbarMessage = stringResource(R.string.dummy_snackbar_text)

        coroutineScope.launch {
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }
}

@Composable
fun ForecastScreenContent(
    viewState: ForecastViewState,
    snackbarHostState: SnackbarHostState,
    onGoToWeatherAlertsClicked: () -> Unit,
    onShowDummySnackbarClicked: () -> Unit,
    onManageLocationsClicked: () -> Unit,
    onSimulateLocationGoneButton: () -> Unit
) {
    BoxWithConstraints {
        val constraints = ConstraintSet {
            val locationHeadline = createRefFor("locationHeadline")
            val weatherSummary = createRefFor("weatherSummary")
            val alertsButton = createRefFor("alertsButton")
            val manageLocationsButton = createRefFor("manageLocationsButton")
            val showDummySbackbarButton = createRefFor("showDummySnackbarButton")
            val simulateLocationGoneButton = createRefFor("simulateLocationGoneButton")
            val snackbar = createRefFor("Snackbar")

            constrain(locationHeadline) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            constrain(weatherSummary) {
                top.linkTo(locationHeadline.bottom)
                start.linkTo(parent.start)
            }
            constrain(alertsButton) {
                top.linkTo(weatherSummary.bottom, margin = Dimensions.PADDING_MEDIUM)
                start.linkTo(parent.start)
            }
            constrain(manageLocationsButton) {
                bottom.linkTo(simulateLocationGoneButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(simulateLocationGoneButton) {
                bottom.linkTo(showDummySbackbarButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(showDummySbackbarButton) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(snackbar) {
                bottom.linkTo(parent.bottom)
            }
        }
        ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
            val locationHeadlineText =
                if (viewState.activeLocation == null) {
                    stringResource(R.string.no_location_selected)
                } else {
                    viewState.activeLocation.name
                }

            if (viewState.weather?.weather != null) {
                Text(
                    text = viewState.weather.weather,
                    modifier = Modifier.layoutId("weatherSummary")
                )
            }

            Text(
                text = locationHeadlineText,
                modifier = Modifier.layoutId("locationHeadline")
            )

            if (viewState.activeLocation != null) {
                Button(
                    modifier = Modifier.layoutId("alertsButton"),
                    onClick = { onGoToWeatherAlertsClicked() }
                ) {
                    Text(text = "Alerts for ${viewState.activeLocation.name}")
                }
            }

            Button(
                modifier = Modifier.layoutId("showDummySnackbarButton"),
                onClick = onShowDummySnackbarClicked
            ) {
                Text(text = stringResource(R.string.show_dummy_snackbar_button))
            }

            Button(
                modifier = Modifier.layoutId("manageLocationsButton"),
                onClick = onManageLocationsClicked
            ) {
                Text(text = stringResource(R.string.manage_locations_button))
            }

            Button(
                modifier = Modifier
                    .layoutId("simulateLocationGoneButton")
                    .testTag("simulateLocationGoneButton"),
                onClick = onSimulateLocationGoneButton
            ) {
                Text(text = stringResource(R.string.simulate_location_gone_button))
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.layoutId("Snackbar")
            )
        }
    }
}

@Composable
@Preview
fun ForecastScreenContentPreview() {
    val activeLocation = Location("Erlangen")
    val viewState = ForecastViewState(
        activeLocation = activeLocation,
        weather = WeatherTO("Snow", activeLocation)
    )
    val snackbarHostState = remember { SnackbarHostState() }
    ForecastScreenContent(viewState, snackbarHostState, {}, {}, {}, {})
}
