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
import de.eso.weather.ui.routing.api.Routes
import de.eso.weather.ui.shared.compose.Dimensions

@Composable
fun LocationSearchScreen(
    navController: NavController,
    locationSearchViewModel: LocationSearchViewModel
) {
    val finishWithResult by locationSearchViewModel.finishWithResult
    finishWithResult?.let { event ->
        event.getContentIfNotHandled()?.let { location ->
            navController.previousBackStackEntry?.savedStateHandle?.set(
                Routes.LOCATION_SEARCH_RESULT,
                location.id
            )
            navController.popBackStack()
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
                .padding(bottom = Dimensions.ContainerPadding)
        )

        LocationGrid(
            isLargeScreen = false,
            locations = locations,
            onLocationSelected = onLocationSelected
        )
    }
}

