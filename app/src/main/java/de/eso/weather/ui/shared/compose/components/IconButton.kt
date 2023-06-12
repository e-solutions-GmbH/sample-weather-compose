package de.eso.weather.ui.shared.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import de.eso.weather.ui.shared.compose.EsoColors
import de.eso.weather.ui.shared.compose.WeatherTheme

@Composable
fun IconAndTextButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = EsoColors.Orange,
    iconSize: Dp = WeatherTheme.dimensions.iconSizeButton,
    textFillsSpace: Boolean = false
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier
                .padding(end = WeatherTheme.dimensions.iconPadding)
                .size(iconSize)
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f, textFillsSpace)
        )
    }
}
