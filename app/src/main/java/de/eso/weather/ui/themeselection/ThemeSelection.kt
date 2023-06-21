package de.eso.weather.ui.themeselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import de.eso.weather.ui.forecast.ForecastScreenActiveLocationForecast
import de.eso.weather.ui.shared.compose.ColorPalette
import de.eso.weather.ui.shared.compose.ColorPalettes
import de.eso.weather.ui.shared.compose.LocalDimensions
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun ThemeSelectionScreen(
    onColorPaletteSelected: (ColorPalette) -> Unit,
    onSizeSelected: (Float) -> Unit
) {
    val isLargeScreen = WeatherTheme.isLargeScreen()

    val boxWidthModifier = if (!isLargeScreen) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
    }

    val cellCount = if (isLargeScreen) 3 else 1

    LazyVerticalGrid(
        modifier = boxWidthModifier,
        columns = GridCells.Fixed(count = cellCount),
        horizontalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding),
        verticalArrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding)
    ) {
        item(span = { GridItemSpan(currentLineSpan = cellCount) }) {
            SizeScaling(onSizeSelected = onSizeSelected)
        }

        item(span = { GridItemSpan(currentLineSpan = cellCount) }) {
            Text(
                "Available themes",
                modifier = Modifier.padding(
                    top = WeatherTheme.dimensions.contentPadding,
                    bottom = WeatherTheme.dimensions.contentPadding
                )
            )
        }

        items(items = ColorPalettes.all, key = { item: ColorPalette -> item.name }) { colorPalette ->
            WeatherTheme(colorPalette = colorPalette) {
                Box(
                    modifier = Modifier
                        .size(WeatherTheme.dimensions.decoratorSize)
                        .clickable { onColorPaletteSelected(colorPalette) }
                ) {
                    ForecastScreenActiveLocationForecast(
                        locationHeadlineText = "Ammerndorf",
                        weatherSummary = "Sunny",
                        activeLocationName = "Ammerndorf",
                        onGoToWeatherAlertsClicked = {}
                    )
                }
            }
        }
    }
}

@Composable
fun SizeScaling(onSizeSelected: (Float) -> Unit) {
    val initialScale = LocalDimensions.current.scale
    var size by remember { mutableStateOf(initialScale) }

    Column {
        Text("Zoom: ${(size * 100).toInt()} %")
        Slider(
            value = size,
            onValueChange = {
                size = it
            },
            valueRange = 0.75f..2f,
            steps = 4,
            onValueChangeFinished = {
                onSizeSelected(size)
            },
            interactionSource = MutableInteractionSource()
        )
    }
}

@Preview
@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun ThemeSelectionScreenPreview() {
    ThemeSelectionScreen(
        onColorPaletteSelected = {},
        onSizeSelected = {}
    )
}