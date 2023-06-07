package de.eso.weather.ui.themeselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.eso.weather.ui.forecast.ForecastScreenActiveLocationForecast
import de.eso.weather.ui.shared.compose.ColorPalette
import de.eso.weather.ui.shared.compose.ColorPalettes
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun ThemeSelectionScreen(
    onColorPaletteSelected: (ColorPalette) -> Unit
) {
    LazyColumn {
        items(ColorPalettes.all) { colorPalette ->
            WeatherTheme(colorPalette = colorPalette) {
                Box(
                    modifier = Modifier
                        .size(300.dp)
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

@Preview
@Composable
fun ThemeSelectionScreenPreview() {
    ThemeSelectionScreen(
        onColorPaletteSelected = {}
    )
}