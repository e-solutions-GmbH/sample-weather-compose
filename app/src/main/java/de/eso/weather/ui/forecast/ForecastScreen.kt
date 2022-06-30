package de.eso.weather.ui.forecast

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
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
        viewModel::onSimulateLocationGoneButton,
        isLargeScreen = WeatherTheme.isLargeScreen()
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
    onSimulateLocationGoneButton: () -> Unit,
    isLargeScreen: Boolean = false
) {
    val locationHeadlineText =
        if (viewState.activeLocation == null) {
            stringResource(R.string.no_location_selected)
        } else {
            viewState.activeLocation.name
        }

    val weatherSummary = viewState.weather?.weather

    val activeLocationName = viewState.activeLocation?.name

    BoxWithConstraints {
        val constraints = ConstraintSet {
            val horizontalSplit = createGuidelineFromStart(0.6f)

            val activeLocationForecastRef = createRefFor("activeLocationForecast")
            val configurationPanelRef = createRefFor("configurationPanel")
            val esoLogoRef = createRefFor("esoLogo")
            val snackbarRef = createRefFor("snackbar")

            if (isLargeScreen) {
                constrain(activeLocationForecastRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(horizontalSplit)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }

                constrain(configurationPanelRef) {
                    top.linkTo(parent.top)
                    start.linkTo(horizontalSplit)
                    end.linkTo(parent.end)
                    bottom.linkTo(esoLogoRef.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            } else {
                constrain(activeLocationForecastRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(configurationPanelRef.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }

                constrain(configurationPanelRef) {
                    top.linkTo(activeLocationForecastRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(esoLogoRef.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            }

            constrain(esoLogoRef) {
                top.linkTo(configurationPanelRef.bottom)
                end.linkTo(parent.end)
                start.linkTo(configurationPanelRef.start)
                bottom.linkTo(parent.bottom)
            }

            constrain(snackbarRef) {
                bottom.linkTo(parent.bottom)
            }
        }

        ConstraintLayout(constraintSet = constraints, modifier = Modifier.fillMaxSize()) {
            ForecastScreenActiveLocationForecast(
                locationHeadlineText = locationHeadlineText,
                weatherSummary = weatherSummary,
                activeLocationName = activeLocationName,
                onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                modifier = Modifier
                    .layoutId("activeLocationForecast")
                    .padding(
                        end = if (isLargeScreen) Dimensions.ContainerPadding else 0.dp,
                        bottom = if (isLargeScreen) 0.dp else Dimensions.ContainerPadding
                    )
            )

            ForecastScreenConfigurationPanel(
                onShowDummySnackbarClicked = onShowDummySnackbarClicked,
                onManageLocationsClicked = onManageLocationsClicked,
                onSimulateLocationGoneButton = onSimulateLocationGoneButton,
                modifier = Modifier.layoutId("configurationPanel")
            )

            ForecastScreenEsoLogo(
                modifier = Modifier
                    .layoutId("esoLogo")
                    .height(Dimensions.IconSizeBigLogo)
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.layoutId("snackbar")
            )
        }
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
            style = MaterialTheme.typography.h4
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
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding),
            textFillsSpace = true
        )

        IconAndTextButton(
            onClick = onSimulateLocationGoneButton,
            imageVector = Icons.Filled.LocationOff,
            text = stringResource(R.string.simulate_location_gone_button),
            contentDescription = stringResource(R.string.simulate_location_gone_button),
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding),
            textFillsSpace = true
        )

        IconAndTextButton(
            onClick = onShowDummySnackbarClicked,
            imageVector = Icons.Filled.Restaurant,
            text = stringResource(R.string.show_dummy_snackbar_button),
            contentDescription = stringResource(R.string.show_dummy_snackbar_button),
            modifier = Modifier.padding(bottom = Dimensions.ButtonPadding),
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

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun ForecastScreenContentPreviewAutomotive() {
    ForecastScreenContentPreview(isLargeScreen = true)
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun ForecastScreenContentPreviewDefault() {
    ForecastScreenContentPreview()
}

@Composable
fun ForecastScreenContentPreview(isLargeScreen: Boolean = false) {
    val activeLocation = Location("Erlangen")
    val viewState = ForecastViewState(
        activeLocation = activeLocation,
        weather = WeatherTO("Snow", activeLocation)
    )
    val snackbarHostState = remember { SnackbarHostState() }

    WeatherTheme {
        ForecastScreenContent(viewState, snackbarHostState, {}, {}, {}, {}, isLargeScreen)
    }
}
