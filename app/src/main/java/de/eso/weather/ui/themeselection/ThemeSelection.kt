package de.eso.weather.ui.themeselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column {
        SizeScaling(onSizeSelected = onSizeSelected)
        ThemeSelection(onColorPaletteSelected)
    }
}

@Composable
private fun ThemeSelection(onColorPaletteSelected: (ColorPalette) -> Unit) {
    Text(
        "Available themes",
        modifier = Modifier.padding(
            top = WeatherTheme.dimensions.contentPadding,
            bottom = WeatherTheme.dimensions.contentPadding
        )
    )

    val boxWidthModifier = if (!WeatherTheme.isLargeScreen()) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
    }

    LazyColumn(
        modifier = boxWidthModifier,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.dimensions.contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(ColorPalettes.all) { colorPalette ->
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

@Preview
@Composable
fun ThemeSelectionScreenPreview() {
    ThemeSelectionScreen(
        onColorPaletteSelected = {},
        onSizeSelected = {}
    )
}