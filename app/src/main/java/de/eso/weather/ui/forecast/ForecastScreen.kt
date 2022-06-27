package de.eso.weather.ui.forecast

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import de.eso.weather.R
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.IconAndTextButton
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
    val locationHeadlineText =
        if (viewState.activeLocation == null) {
            stringResource(R.string.no_location_selected)
        } else {
            viewState.activeLocation.name
        }

    val weatherSummary = viewState.weather?.weather

    val activeLocationName = viewState.activeLocation?.name

    Box {
        if (WeatherTheme.isLargeScreen()) {
            Row(modifier = Modifier.fillMaxSize()) {
                ForecastScreenActiveLocationForecast(
                    locationHeadlineText = locationHeadlineText,
                    weatherSummary = weatherSummary,
                    activeLocationName = activeLocationName,
                    onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                        .padding(end = Dimensions.ContainerPadding)
                )

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                ) {
                    ForecastScreenConfigurationPanel(
                        onShowDummySnackbarClicked = onShowDummySnackbarClicked,
                        onManageLocationsClicked = onManageLocationsClicked,
                        onSimulateLocationGoneButton = onSimulateLocationGoneButton
                    )

                    ForecastScreenEsoLogo(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(align = Alignment.Bottom)
                            .height(Dimensions.IconSizeBigLogo)
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                ForecastScreenActiveLocationForecast(
                    locationHeadlineText = locationHeadlineText,
                    weatherSummary = weatherSummary,
                    activeLocationName = activeLocationName,
                    onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(bottom = Dimensions.ContainerPadding)
                )

                ForecastScreenConfigurationPanel(
                    onShowDummySnackbarClicked = onShowDummySnackbarClicked,
                    onManageLocationsClicked = onManageLocationsClicked,
                    onSimulateLocationGoneButton = onSimulateLocationGoneButton,
                    modifier = Modifier.weight(1f)
                )

                ForecastScreenEsoLogo(
                    modifier = Modifier.height(Dimensions.IconSizeLogo)
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ForecastScreenActiveLocationForecast(
    locationHeadlineText: String,
    weatherSummary: String?,
    activeLocationName: String?,
    onGoToWeatherAlertsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = EsoColors.White.copy(alpha = 0.502f))
            .background(
                brush = Brush.verticalGradient(
                    Pair(0f, EsoColors.DarkBlue),
                    Pair(0.7f, EsoColors.DarkBlue.copy(alpha = 0.8f)),
                    Pair(1f, EsoColors.DarkBlue.copy(alpha = 0.3f))
                )
            )
            .padding(all = Dimensions.ContainerPadding)
    ) {
        Text(
            text = locationHeadlineText,
            style = WeatherTheme.largeScreenTypography.h4
        )

        weatherSummary?.let {
            Text(
                text = weatherSummary,
                modifier = Modifier.weight(weight = 1f)
            )
        }

        activeLocationName?.let {
            val buttonText = "Alerts for $it"

            IconAndTextButton(
                onClick = { onGoToWeatherAlertsClicked() },
                imageVector = Icons.Filled.Warning,
                text = buttonText,
                contentDescription = buttonText,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ForecastScreenConfigurationPanel(
    onShowDummySnackbarClicked: () -> Unit,
    onManageLocationsClicked: () -> Unit,
    onSimulateLocationGoneButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        IconAndTextButton(
            onClick = onManageLocationsClicked,
            imageVector = Icons.Filled.LocationCity,
            text = stringResource(R.string.manage_locations_button),
            contentDescription = stringResource(R.string.manage_locations_button),
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding).fillMaxWidth(),
            textFillsSpace = true
        )

        IconAndTextButton(
            onClick = onSimulateLocationGoneButton,
            imageVector = Icons.Filled.LocationOff,
            text = stringResource(R.string.simulate_location_gone_button),
            contentDescription = stringResource(R.string.simulate_location_gone_button),
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding).fillMaxWidth(),
            textFillsSpace = true
        )

        IconAndTextButton(
            onClick = onShowDummySnackbarClicked,
            imageVector = Icons.Filled.Restaurant,
            text = stringResource(R.string.show_dummy_snackbar_button),
            contentDescription = stringResource(R.string.show_dummy_snackbar_button),
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding).fillMaxWidth(),
            textFillsSpace = true
        )
    }
}

@Composable
fun ForecastScreenEsoLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_eso_logo_colour_negative_rgb),
        contentDescription = "Eso logo",
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ForecastScreenContentDefaultScreenSize(
    locationHeadlineText: String,
    weatherSummary: String?,
    activeLocationName: String?,
    snackbarHostState: SnackbarHostState,
    onGoToWeatherAlertsClicked: () -> Unit,
    onShowDummySnackbarClicked: () -> Unit,
    onManageLocationsClicked: () -> Unit,
    onSimulateLocationGoneButton: () -> Unit
) {
    BoxWithConstraints {
        val constraints = ConstraintSet {
            val locationHeadline = createRefFor("locationHeadline")
            val weatherSummaryRef = createRefFor("weatherSummary")
            val alertsButton = createRefFor("alertsButton")
            val manageLocationsButton = createRefFor("manageLocationsButton")
            val showDummySbackbarButton = createRefFor("showDummySnackbarButton")
            val simulateLocationGoneButton = createRefFor("simulateLocationGoneButton")
            val snackbar = createRefFor("Snackbar")

            constrain(locationHeadline) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            constrain(weatherSummaryRef) {
                top.linkTo(locationHeadline.bottom)
                start.linkTo(parent.start)
            }
            constrain(alertsButton) {
                top.linkTo(weatherSummaryRef.bottom, margin = Dimensions.PADDING_MEDIUM)
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
            weatherSummary?.let {
                Text(
                    text = it,
                    modifier = Modifier.layoutId("weatherSummary")
                )
            }

            Text(
                text = locationHeadlineText,
                modifier = Modifier.layoutId("locationHeadline")
            )

            activeLocationName?.let {
                Button(
                    modifier = Modifier.layoutId("alertsButton"),
                    onClick = { onGoToWeatherAlertsClicked() }
                ) {
                    Text(text = "Alerts for $it")
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

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun ForecastScreenContentPreviewAutomotive() {
    ForecastScreenContentPreview()
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun ForecastScreenContentPreviewDefault() {
    ForecastScreenContentPreview()
}

@Composable
fun ForecastScreenContentPreview() {
    val activeLocation = Location("Erlangen")
    val viewState = ForecastViewState(
        activeLocation = activeLocation,
        weather = WeatherTO("Snow", activeLocation)
    )
    val snackbarHostState = remember { SnackbarHostState() }

    WeatherTheme {
        ForecastScreenContent(viewState, snackbarHostState, {}, {}, {}, {})
    }
}
