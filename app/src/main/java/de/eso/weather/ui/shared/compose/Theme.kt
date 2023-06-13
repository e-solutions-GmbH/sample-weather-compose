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
    fun createTypography(isLargeScreen: Boolean = false): Typography {
        val defaultFontFamily = FontFamily(Font(R.font.calibri))
        val headlineFontFamily = FontFamily(Font(R.font.cambria))

        val commonTypography = Typography(
            defaultFontFamily = defaultFontFamily,
            h4 = TextStyle(fontFamily = headlineFontFamily, color = EsoColors.Orange),
            h6 = TextStyle(fontFamily = headlineFontFamily, color = EsoColors.Orange)
        )

        // Choose fitting typography depending on screen size (e.g. larger for Automotive resolutions)
        val displaySizeAdaptedTypography = if (isLargeScreen) {
            commonTypography.copy(
                h4 = commonTypography.h4.copy(fontSize = dimensions.headerTextSize),
                h6 = commonTypography.h6.copy(fontSize = dimensions.titleTextSize),
                subtitle1 = commonTypography.subtitle1.copy(fontSize = dimensions.subTitleTextSize),
                body1 = commonTypography.body1.copy(fontSize = dimensions.body1TextSize),
                body2 = commonTypography.body2.copy(fontSize = dimensions.body2TextSize),
                button = commonTypography.button.copy(fontSize = dimensions.buttonTextSize),
                caption = commonTypography.caption.copy(fontSize = dimensions.captionTextSize)
            )
        } else {
            commonTypography.copy(
                h4 = commonTypography.h4.copy(fontSize = dimensions.headerTextSize),
                h6 = commonTypography.h6.copy(fontSize = dimensions.titleTextSize),
                subtitle1 = commonTypography.subtitle1.copy(fontSize = dimensions.subTitleTextSize),
                body1 = commonTypography.body1.copy(fontSize = dimensions.body1TextSize),
                body2 = commonTypography.body2.copy(fontSize = dimensions.body2TextSize),
                button = commonTypography.button.copy(fontSize = dimensions.buttonTextSize),
                caption = commonTypography.caption.copy(fontSize = dimensions.captionTextSize)
            )
        }

        return displaySizeAdaptedTypography
    }
}

@Immutable
data class ScreenSize(
    val isLargeScreen: Boolean
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
            typography = WeatherTheme.createTypography(isLargeScreen)
        )
    }
}
