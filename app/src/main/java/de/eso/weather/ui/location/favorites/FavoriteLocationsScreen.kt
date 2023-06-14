package de.eso.weather.ui.location.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.location.shared.LocationGrid
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.livedatacommand.LiveDataCommand

@Composable
fun LiveData<LiveDataCommand>.observeCommandAsState(action: () -> Unit) {
    val commandAsState by observeAsState()
    if (commandAsState?.hasBeenHandled == false) {
        action()
        commandAsState?.hasBeenHandled = true
    }
}

@Composable
fun FavoriteLocationsScreen(
    viewModel: FavoriteLocationsViewModel,
    navController: NavController
) {
    viewModel.finishScreen.observeCommandAsState {
        navController.popBackStack()
    }

    val locations by viewModel.locations.subscribeAsState(emptyList())
    FavoriteLocationsScreenContent(
        locations = locations,
        onLocationSelected = viewModel::onLocationSelected,
        onAddLocationClicked = {
            navController.navigate(Routes.LOCATION_SEARCH)
        },
        isLargeScreen = WeatherTheme.isLargeScreen()
    )
}

@Composable
fun FavoriteLocationsScreenContent(
    locations: List<Location>,
    onLocationSelected: (Location) -> Unit,
    onAddLocationClicked: () -> Unit,
    isLargeScreen: Boolean = false
) {
    Box {
        LocationGrid(isLargeScreen, locations, onLocationSelected)

        FloatingActionButton(
            modifier = Modifier.align(alignment = Alignment.BottomEnd),
            onClick = onAddLocationClicked
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Location",
                tint = WeatherTheme.colorPalette.iconTint
            )
        }
    }
}


@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun FavoriteLocationsScreenContentPreviewLarge() {
    FavoriteLocationsScreenContentPreview(isLargeScreen = true)
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun FavoriteLocationsScreenContentPreviewDefault() {
    FavoriteLocationsScreenContentPreview()
}

@Composable
fun FavoriteLocationsScreenContentPreview(isLargeScreen: Boolean = false) {
    WeatherTheme {
        FavoriteLocationsScreenContent(
            listOf(
                Location("Erlangen"),
                Location("Bamberg"),
                Location("Nürnberg"),
                Location("Fürth"),
                Location("Lauf")
            ),
            {},
            {},
            isLargeScreen
        )
    }
}
