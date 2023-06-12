package de.eso.weather.ui.shared.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun Tile(
    borderColor: Color = WeatherTheme.colorPalette.colors.onSecondary,
    backgroundColor: Color = WeatherTheme.colorPalette.colors.secondaryVariant,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = borderColor.copy(alpha = 0.502f))
            .background(
                brush = Brush.verticalGradient(
                    Pair(0f, backgroundColor.copy(alpha = 0.8f)),
                    Pair(0.75f, backgroundColor),
                    Pair(1f, backgroundColor)
                )
            )
            .padding(all = WeatherTheme.dimensions.containerPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Preview
@Composable
fun TilePreview() {
    Tile(
        backgroundColor = Color.White,
        borderColor = Color.Black,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(text = "Hello Tile")
    }
}
