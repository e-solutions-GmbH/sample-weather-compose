package de.eso.weather.ui.forecast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import de.eso.weather.R
import de.eso.weather.domain.forecast.api.WeatherTO
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.shared.compose.ColorPalette
import de.eso.weather.ui.shared.compose.ColorPalettes
import de.eso.weather.ui.shared.compose.FontStyle
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.AdaptiveStaggeredGrid
import de.eso.weather.ui.shared.compose.components.AddItemTile
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
                    checkNotEmptyForecastViewState(viewModel.forecastViewState).activeLocation.id
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
    isLargeScreen: Boolean = WeatherTheme.isLargeScreen(),
    onGoToWeatherAlertsClicked: () -> Unit,
    onAddLocationClicked: () -> Unit
) {
    Box {
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
    isLargeScreen: Boolean = WeatherTheme.isLargeScreen(),
    onGoToWeatherAlertsClicked: () -> Unit,
    onAddLocationClicked: () -> Unit
) {
    // Exclude the active location from the list of saved locations, so it is not displayed twice.
    val savedLocationWithoutActiveLocation = remember(activeLocationViewState) {
        savedLocationsViewState.filter {
            it.activeLocation.id != activeLocationViewState.activeLocation.id
        }
    }

    val locationHeadlineText = activeLocationViewState.activeLocation.name

    val weatherSummary = activeLocationViewState.weather.weather
    val activeLocationName = activeLocationViewState.activeLocation.name

    val activeTileModifier = when {
        isLargeScreen -> Modifier
            .width(width = WeatherTheme.dimensions.tileSizeLarge)
            .fillMaxHeight()
        else -> Modifier
            .height(height = WeatherTheme.dimensions.tileSizeLarge)
            .fillMaxWidth()
    }

    val tileModifier = when {
        isLargeScreen -> Modifier.width(width = WeatherTheme.dimensions.tileSize)
        else -> Modifier.height(height = WeatherTheme.dimensions.tileSize)
    }

    AdaptiveStaggeredGrid(modifier = modifier) {
        if (activeLocationViewState != EmptyForecastViewState) {
            item(span = StaggeredGridItemSpan.FullLine) {
                ForecastScreenActiveLocationForecast(
                    locationHeadlineText = locationHeadlineText,
                    weatherSummary = weatherSummary,
                    activeLocationName = activeLocationName,
                    onGoToWeatherAlertsClicked = onGoToWeatherAlertsClicked,
                    modifier = activeTileModifier
                )
            }
        }

        items(savedLocationWithoutActiveLocation) {
            ForecastScreenSavedLocationForecast(
                locationName = it.activeLocation.name,
                weatherSummary = it.weather.weather,
                modifier = tileModifier
            )
        }

        item {
            AddItemTile(
                text = stringResource(id = R.string.location_add_title),
                imageVector = Icons.Filled.Add,
                onClick = onAddLocationClicked,
                modifier = tileModifier
            )
        }
    }
}

@Composable
fun ForecastScreenActiveLocationForecast(
    locationHeadlineText: String,
    weatherSummary: String,
    activeLocationName: String,
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
            Text(
                text = weatherSummary,
                modifier = Modifier.weight(weight = 1f),
                style = WeatherTheme.typography.body1
            )
            Icon(
                painter = painterResource(id = weatherIcon),
                contentDescription = weatherSummary,
                tint = WeatherTheme.colorPalette.iconTint,
                modifier = Modifier
                    .weight(weight = 2f)
                    .fillMaxHeight()
                    .width(width = WeatherTheme.dimensions.decoratorSize)
                    .padding(bottom = WeatherTheme.dimensions.iconPadding)
            )
        }

        val buttonText = "Alerts for $activeLocationName"

        IconAndTextButton(
            onClick = { onGoToWeatherAlertsClicked() },
            imageVector = Icons.Filled.Warning,
            text = buttonText,
            contentDescription = buttonText,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
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
                tint = WeatherTheme.colorPalette.iconTint,
                modifier = Modifier
                    .size(size = WeatherTheme.dimensions.iconSizeButton)
                    .padding(end = WeatherTheme.dimensions.iconPadding)
            )
            Text(text = weatherSummary)
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

data class OemSkin(
    val colorPalette: ColorPalette,
    val fontStyle: FontStyle = FontStyle(),
    val dimensionScale: Float = 1.0f
)

class OemSkinProvider : CollectionPreviewParameterProvider<OemSkin>(
    listOf(
        OemSkin(colorPalette = ColorPalettes.DarkBlue),
        OemSkin(
            colorPalette = ColorPalettes.DarkBlue,
            fontStyle = FontStyle(
                defaultFontFamily = FontFamily.Cursive,
                headlineFontFamily = FontFamily.Monospace
            )
        ),
        // ...
        OemSkin(
            colorPalette = ColorPalettes.DarkGreen,
            fontStyle = FontStyle(
                defaultFontFamily = FontFamily.Cursive,
                headlineFontFamily = FontFamily.Monospace
            )
        ),
        OemSkin(
            colorPalette = ColorPalettes.Violet.copy(iconTint = Color.Magenta.copy(alpha = 0.65f)),
            fontStyle = FontStyle(
                defaultFontFamily = FontFamily.Serif,
                headlineFontFamily = FontFamily.SansSerif,
                headerColor = Color.Magenta.copy(alpha = 0.65f)
            )
        )
    )
)

@Preview(widthDp = 300, heightDp = 120)
@Composable
fun ForecastScreenSavedLocationForecastPreview(@PreviewParameter(OemSkinProvider::class) oemSkin: OemSkin) {
    WeatherTheme(
        colorPalette = oemSkin.colorPalette,
        fontStyle = oemSkin.fontStyle,
        dimensionScale = oemSkin.dimensionScale
    ) {
        ForecastScreenSavedLocationForecast(locationName = "Erlangen", weatherSummary = "Thunderstorm")
    }
}

// @Preview(device = Devices.PHONE, widthDp = 500, heightDp = 900)
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 800, heightDp = 450)
@Composable
fun ForecastScreenContentPreview(
    @PreviewParameter(OemSkinProvider::class) oemSkin: OemSkin
) {
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

    WeatherTheme(
        colorPalette = oemSkin.colorPalette,
        fontStyle = oemSkin.fontStyle,
        dimensionScale = oemSkin.dimensionScale
    ) {
        GridBackground()
        ForecastScreenContent(
            // ---
            viewState = viewState,
            savedLocationsViewState = savedLocations.map { location ->
                ForecastViewState(
                    activeLocation = location,
                    weather = WeatherTO(weather = "Sunshine", location = location)
                )
            },
            onAddLocationClicked = {},
            onGoToWeatherAlertsClicked = {}
        )
    }
}
