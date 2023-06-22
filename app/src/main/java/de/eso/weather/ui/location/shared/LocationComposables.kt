package de.eso.weather.ui.location.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.compose.WeatherTheme
import de.eso.weather.ui.shared.compose.components.Tile

@Composable
fun LocationGrid(
    isLargeScreen: Boolean,
    locations: List<Location>,
    onLocationSelected: (Location) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = if (isLargeScreen) 3 else 2),
        verticalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding),
        horizontalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding),
        content = {
            items(items = locations, key = { item: Location -> item.id }) { location ->
                LocationTile(
                    locationName = location.name,
                    onClick = { onLocationSelected(location) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun LocationGrid(
    isLargeScreen: Boolean,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = if (isLargeScreen) 3 else 2),
        verticalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding),
        horizontalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding),
        content = content,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun LocationTile(
    locationName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Tile(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(height = WeatherTheme.dimensions.tileSizeSmall)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = locationName,
            style = WeatherTheme.typography.body1
        )
    }
}

