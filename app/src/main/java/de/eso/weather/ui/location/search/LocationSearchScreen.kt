package de.eso.weather.ui.location.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.location.shared.LocationGrid
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun LocationSearchScreen(
    navController: NavController,
    locationSearchViewModel: LocationSearchViewModel
) {
    val finishWithResult by locationSearchViewModel.finishScreen
    finishWithResult?.let { command ->
        if (!command.hasBeenHandled) {
            navController.popBackStack()
            command.hasBeenHandled = true
        }
    }

    val queryInput by locationSearchViewModel.queryInput
    val locations by locationSearchViewModel.locations
    LocationSearchScreenContent(
        query = queryInput,
        onQueryChanged = locationSearchViewModel::onQueryChanged,
        locations = locations,
        onLocationSelected = locationSearchViewModel::onLocationSelected
    )
}

@Composable
fun LocationSearchScreenContent(
    query: String,
    onQueryChanged: (String) -> Unit,
    locations: List<Location>,
    onLocationSelected: (Location) -> Unit
) {
    Column {
        TextField(
            value = query,
            label = {
                Text(text = "Filter locations")
            },
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WeatherTheme.dimensions.containerPadding)
        )

        LocationGrid(
            isLargeScreen = false,
            locations = locations,
            onLocationSelected = onLocationSelected
        )
    }
}

