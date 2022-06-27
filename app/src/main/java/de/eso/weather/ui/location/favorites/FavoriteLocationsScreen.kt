package de.eso.weather.ui.location.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.LiveData
import de.eso.weather.ui.shared.livedatacommand.LiveDataCommand
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.Dimensions
import de.eso.weather.ui.shared.compose.WeatherTheme

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
        viewModel::onEditLocationsClicked
    )
}

@Composable
fun FavoriteLocationsScreenContent(
    locations: List<Location>,
    onLocationSelected: (Location) -> Unit,
    onAddLocationClicked: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val constraints = ConstraintSet {
            val locationList = createRefFor("locationList")
            val addLocationButton = createRefFor("addLocationButton")

            constrain(locationList) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }

            constrain(addLocationButton) {
                bottom.linkTo(parent.bottom, margin = Dimensions.PADDING_MEDIUM)
                end.linkTo(parent.end, margin = Dimensions.PADDING_MEDIUM)
            }
        }

        ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .layoutId("locationList")
                    .fillMaxWidth(),
                content = {
                    items(locations, key = { it.id }) { location ->
                        Text(
                            text = location.name,
                            modifier = Modifier
                                .clickable(onClick = { onLocationSelected(location) })

                        )
                    }
                })

            FloatingActionButton(
                modifier = Modifier.layoutId("addLocationButton"),
                onClick = onAddLocationClicked
            ) {
                Text(text = "+")
            }
        }
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Preview(device = Devices.PIXEL_4)
@Composable
fun FavoriteLocationsScreenContentPreview() {
    WeatherTheme {
        FavoriteLocationsScreenContent(listOf(Location("Erlangen")), {}, {})
    }
}
