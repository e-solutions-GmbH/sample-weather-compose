package de.eso.weather.ui.shared.compose

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import de.eso.weather.R

object WeatherTheme {
    @Composable
    fun isLargeScreen() = LocalScreenSize.current.isLargeScreen

    val colorPalette: ColorPalette
        @Composable
        get() = LocalColorPalette.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val dimensions: Dimensions
        @Composable
        get() = LocalDimensions.current

    @Composable
    fun createTypography(fontStyle: FontStyle): Typography {
        return Typography(
            defaultFontFamily = fontStyle.defaultFontFamily,
            h4 = TextStyle(fontFamily = fontStyle.headlineFontFamily, color = fontStyle.headerColor, fontSize = dimensions.headerTextSize),
            h6 = TextStyle(fontFamily = fontStyle.headlineFontFamily, color = fontStyle.headerColor, fontSize = dimensions.titleTextSize),
            subtitle1 = TextStyle(fontSize = dimensions.subTitleTextSize, color = fontStyle.contentColor),
            body1 = TextStyle(fontSize = dimensions.body1TextSize, color = fontStyle.contentColor),
            body2 = TextStyle(fontSize = dimensions.body2TextSize, color = fontStyle.contentColor),
            button = TextStyle(fontSize = dimensions.buttonTextSize, color = fontStyle.contentColor),
            caption = TextStyle(fontSize = dimensions.captionTextSize, color = fontStyle.contentColor)
        )
    }
}

@Immutable
data class ScreenSize(
    val isLargeScreen: Boolean
)

@Immutable
data class FontStyle(
    val defaultFontFamily: FontFamily = FontFamily(Font(R.font.calibri)),
    val headlineFontFamily: FontFamily = FontFamily(Font(R.font.cambria)),
    val headerColor: Color = EsoColors.Orange,
    val contentColor: Color = EsoColors.White
)

val LocalScreenSize = staticCompositionLocalOf {
    ScreenSize(isLargeScreen = false)
}

val LocalColorPalette = compositionLocalOf { ColorPalettes.DarkBlue }

val LocalDimensions = staticCompositionLocalOf { Dimensions.Phone }

@Composable
fun WeatherTheme(
    isLargeScreen: Boolean = false,
    colorPalette: ColorPalette = ColorPalettes.DarkBlue,
    fontStyle: FontStyle = FontStyle(),
    dimensionScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val screenSize = ScreenSize(
        isLargeScreen = LocalConfiguration.current.isLayoutSizeAtLeast(SCREENLAYOUT_SIZE_LARGE)
    )

    val screenSizeDependentDimensions = if (isLargeScreen) {
        Dimensions.Automotive
    } else {
        Dimensions.Phone
    }

    CompositionLocalProvider(
        LocalScreenSize provides screenSize,
        LocalColorPalette provides colorPalette,
        LocalDimensions provides screenSizeDependentDimensions.scale(dimensionScale)
    ) {
        MaterialTheme(
            content = content,
            colors = colorPalette.colors,
            typography = WeatherTheme.createTypography(fontStyle)
        )
    }
}
