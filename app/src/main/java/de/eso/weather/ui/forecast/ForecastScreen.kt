package de.eso.weather.ui.forecast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.GridBackground
import de.eso.weather.ui.shared.compose.components.IconAndTextButton
import de.eso.weather.ui.shared.compose.components.Tile

@Composable
fun ForecastScreen(navController: NavController, viewModel: ForecastViewModel) {
    ForecastScreenContent(
        viewModel.forecastViewState,
        viewModel.forecastSavedLocationsViewState,
        onGoToWeatherAlertsClicked = {
            navController.navigate(
                Routes.toAlerts(
                    checkNotNull(viewModel.forecastViewState.activeLocation?.id)
                )
            )
        },
        onAddLocationClicked = {
            navController.navigate(Routes.LOCATION_SEARCH)
        },
        isLargeScreen = WeatherTheme.isLargeScreen()
    )
}

@Composable
fun ForecastScreenContent(
    viewState: ForecastViewState,
    savedLocationsViewState: List<ForecastViewState>,
    onGoToWeatherAlertsClicked: () -> Unit,
    onAddLocationClicked: () -> Unit,
    isLargeScreen: Boolean = false
) {
    BoxWithConstraints {
        val constraints = ConstraintSet {
            val locationForecastRef = createRefFor("locationForecast")
            val esoLogoRef = createRefFor("esoLogo")

            constrain(locationForecastRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)

                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }

            constrain(esoLogoRef) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)

                width = Dimension.wrapContent
            }
        }

        ConstraintLayout(constraintSet = constraints, modifier = Modifier.fillMaxSize()) {
            ForecastScreenEsoLogo(
                modifier = Modifier
                    .layoutId("esoLogo")
                    .height(if (isLargeScreen) WeatherTheme.dimensions.iconSizeBigLogo else WeatherTheme.dimensions.iconSizeLogo)
                    .width(width = 400.dp)
            )

            ForecastScreenLocationsGrid(
                activeLocationViewState = viewState,
                savedLocationsViewState = savedLocationsViewState,
                onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                onAddLocationClicked = onAddLocationClicked,
                modifier = Modifier.layoutId("locationForecast")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastScreenLocationsGrid(
    modifier: Modifier = Modifier,
    activeLocationViewState: ForecastViewState,
    savedLocationsViewState: List<ForecastViewState>,
    onGoToWeatherAlertsClicked: () -> Unit,
    onAddLocationClicked: () -> Unit
) {
    val locationHeadlineText =
        if (activeLocationViewState.activeLocation == null) {
            stringResource(R.string.no_location_selected)
        } else {
            activeLocationViewState.activeLocation.name
        }

    val weatherSummary = activeLocationViewState.weather?.weather
    val activeLocationName = activeLocationViewState.activeLocation?.name

    LazyHorizontalStaggeredGrid(
        modifier = modifier,
        rows = StaggeredGridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.containerPadding),
        horizontalItemSpacing = WeatherTheme.dimensions.containerPadding
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            ForecastScreenActiveLocationForecast(
                locationHeadlineText = locationHeadlineText,
                weatherSummary = weatherSummary,
                activeLocationName = activeLocationName,
                onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                modifier = Modifier
                    .width(width = WeatherTheme.dimensions.tileSizeLarge)
                    .fillMaxHeight()
            )
        }

        items(savedLocationsViewState) {
            ForecastScreenSavedLocationForecast(
                locationName = it.activeLocation?.name ?: "",
                weatherSummary = it.weather?.weather ?: "",
                modifier = Modifier
                    .width(width = WeatherTheme.dimensions.tileSize)
            )
        }

        item {
            ForecastScreenAddLocation(
                modifier = Modifier
                    .width(width = WeatherTheme.dimensions.tileSize)
                    .clickable { onAddLocationClicked() }
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
    val weatherIcon = when (weatherSummary) {
        "Sunshine" -> R.drawable.ic_forecast_sun
        "Cloudy" -> R.drawable.ic_forecast_cloudy
        "Rain" -> R.drawable.ic_forecast_rain
        "Snow" -> R.drawable.ic_forecast_snow
        "Thunderstorm" -> R.drawable.ic_forecast_thunderstorm
        else -> R.drawable.ic_forecast_sun
    }

    Tile(modifier = modifier) {
        Text(
            text = locationHeadlineText,
            style = WeatherTheme.typography.h4
        )

        Row(modifier = Modifier.weight(1f)) {
            weatherSummary?.let {
                Text(
                    text = weatherSummary,
                    modifier = Modifier.weight(weight = 1f)
                )
                Icon(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = weatherSummary,
                    tint = EsoColors.Orange,
                    modifier = Modifier
                        .weight(weight = 2f)
                        .fillMaxHeight()
                        .width(width = WeatherTheme.dimensions.decoratorSize)
                        .padding(bottom = WeatherTheme.dimensions.iconPadding)
                )
            }
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
fun ForecastScreenSavedLocationForecast(
    modifier: Modifier = Modifier,
    locationName: String,
    weatherSummary: String
) {
    val weatherIcon = when (weatherSummary) {
        "Sunshine" -> R.drawable.ic_forecast_sun
        "Cloudy" -> R.drawable.ic_forecast_cloudy
        "Rain" -> R.drawable.ic_forecast_rain
        "Snow" -> R.drawable.ic_forecast_snow
        "Thunderstorm" -> R.drawable.ic_forecast_thunderstorm
        else -> R.drawable.ic_forecast_sun
    }

    Tile(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = locationName,
            style = WeatherTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WeatherTheme.dimensions.titlePadding)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = weatherIcon),
                contentDescription = "",
                tint = EsoColors.Orange,
                modifier = Modifier
                    .size(size = WeatherTheme.dimensions.iconSizeButton)
                    .padding(end = WeatherTheme.dimensions.iconPadding)
            )
            Text(text = weatherSummary)
        }
    }
}

@Composable
fun ForecastScreenAddLocation(
    modifier: Modifier = Modifier
) {
    Tile(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        borderColor = WeatherTheme.colorPalette.colors.onPrimary,
        backgroundColor = WeatherTheme.colorPalette.colors.primaryVariant
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(size = WeatherTheme.dimensions.iconSizeButton)
                    .padding(end = WeatherTheme.dimensions.iconPadding)
            )
            Text(text = "Add Location")
        }
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

    val savedLocations = listOf(
        Location(name = "Nürnberg"),
        Location(name = "Fürth"),
        Location(name = "Heroldsberg"),
        Location(name = "Tennenlohe"),
    )

    WeatherTheme {
        GridBackground()
        ForecastScreenContent(
            viewState,
            savedLocations.map { location ->
                ForecastViewState(
                    activeLocation = location,
                    weather = WeatherTO(weather = "Sunshine", location = location)
                )
            },
            {},
            {},
            isLargeScreen
        )
    }
}
