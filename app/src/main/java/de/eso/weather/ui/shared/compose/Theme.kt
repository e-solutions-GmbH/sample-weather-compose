package de.eso.weather.ui.shared.compose

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
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
            displayLarge = TextStyle(fontFamily = fontStyle.headlineFontFamily, color = fontStyle.headerColor, fontSize = dimensions.headerTextSize),
            titleLarge = TextStyle(fontFamily = fontStyle.headlineFontFamily, color = fontStyle.headerColor, fontSize = dimensions.headerTextSize),
            headlineMedium = TextStyle(fontFamily = fontStyle.headlineFontFamily, color = fontStyle.headerColor, fontSize = dimensions.titleTextSize),
            titleMedium = TextStyle(fontSize = dimensions.subTitleTextSize, color = fontStyle.contentColor),
            bodyLarge = TextStyle(fontSize = dimensions.body1TextSize, color = fontStyle.contentColor),
            bodyMedium = TextStyle(fontSize = dimensions.body2TextSize, color = fontStyle.contentColor),
            labelLarge = TextStyle(fontSize = dimensions.buttonTextSize, color = fontStyle.contentColor),
            labelSmall = TextStyle(fontSize = dimensions.captionTextSize, color = fontStyle.contentColor)
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

    val colors = darkColorScheme().copy()

    CompositionLocalProvider(
        LocalScreenSize provides screenSize,
        LocalColorPalette provides colorPalette,
        LocalDimensions provides screenSizeDependentDimensions.scale(dimensionScale)
    ) {
        MaterialTheme(
            content = content,
            colorScheme = colorPalette.colorScheme,
            typography = WeatherTheme.createTypography(fontStyle)
        )
    }
}
