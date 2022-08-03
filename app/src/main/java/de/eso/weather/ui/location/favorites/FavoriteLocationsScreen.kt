package de.eso.weather.ui.location.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.Tile
import de.eso.weather.ui.shared.livedatacommand.LiveDataCommand

// TODO Als SideEffect lösen! So ist es bestimmt keine gute Idee!
@Composable
fun LiveData<LiveDataCommand>.observeCommandAsState(action: () -> Unit) {
    val commandAsState by observeAsState()
    if (commandAsState?.hasBeenHandled == false) {
        action()
        commandAsState?.hasBeenHandled = true
    }
}

@Composable
fun FavoriteLocationsScreen(viewModel: FavoriteLocationsViewModel, onShowLocationActivity: () -> Unit, onPopBackStack: () -> Unit) {
    viewModel.showLocationActivity.observeCommandAsState(onShowLocationActivity)
    viewModel.finishScreen.observeCommandAsState(onPopBackStack)

    val locations by viewModel.locations.subscribeAsState(emptyList())
    FavoriteLocationsScreenContent(
        locations,
        viewModel::onLocationSelected,
        viewModel::onEditLocationsClicked,
        WeatherTheme.isLargeScreen()
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteLocationsScreenContent(
    locations: List<Location>,
    onLocationSelected: (Location) -> Unit,
    onAddLocationClicked: () -> Unit,
    isLargeScreen: Boolean = false
) {
    Box {
        LazyVerticalGrid(
            cells = GridCells.Fixed(count = if (isLargeScreen) 3 else 2),
            verticalArrangement = Arrangement.spacedBy(space = Dimensions.ContainerPadding),
            horizontalArrangement = Arrangement.spacedBy(space = Dimensions.ContainerPadding),
            content = {
                items(items = locations) { location ->
                    FavoriteLocationTile(
                        locationName = location.name,
                        onClick = { onLocationSelected(location) }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            modifier = Modifier.align(alignment = Alignment.BottomEnd),
            onClick = onAddLocationClicked
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Location",
                tint = EsoColors.Orange
            )
        }
    }
}

@Composable
fun FavoriteLocationTile(
    locationName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Tile(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(height = Dimensions.TileSizeSmall)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = locationName,
            style = MaterialTheme.typography.body1
        )
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
