package de.eso.weather.ui.shared.compose

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import de.eso.weather.R

object WeatherTheme {
    @Composable
    fun isLargeScreen() = LocalScreenSize.current.isLargeScreen

    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

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
                h4 = commonTypography.h4.copy(fontSize = Dimensions.HeaderTextSizeLarge),
                h6 = commonTypography.h6.copy(fontSize = Dimensions.TitleTextSizeLarge),
                subtitle1 = commonTypography.subtitle1.copy(fontSize = Dimensions.SubTitleTextSizeLarge),
                body1 = commonTypography.body1.copy(fontSize = Dimensions.Body1TextSizeLarge),
                body2 = commonTypography.body2.copy(fontSize = Dimensions.Body2TextSizeLarge),
                button = commonTypography.button.copy(fontSize = Dimensions.ButtonTextSizeLarge),
                caption = commonTypography.caption.copy(fontSize = Dimensions.CaptionTextSizeLarge)
            )
        } else {
            commonTypography.copy(
                h4 = commonTypography.h4.copy(fontSize = Dimensions.HeaderTextSize),
                h6 = commonTypography.h6.copy(fontSize = Dimensions.TitleTextSize),
                subtitle1 = commonTypography.subtitle1.copy(fontSize = Dimensions.SubTitleTextSize),
                body1 = commonTypography.body1.copy(fontSize = Dimensions.Body1TextSize),
                body2 = commonTypography.body2.copy(fontSize = Dimensions.Body2TextSize),
                button = commonTypography.button.copy(fontSize = Dimensions.ButtonTextSize),
                caption = commonTypography.caption.copy(fontSize = Dimensions.CaptionTextSize)
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

@Composable
fun WeatherTheme(
    isLargeScreen: Boolean = false,
    colorPalette: ColorPalette = ColorPalettes.DarkBlue,
    content: @Composable () -> Unit
) {
    val screenSize = ScreenSize(
        isLargeScreen = LocalConfiguration.current.isLayoutSizeAtLeast(SCREENLAYOUT_SIZE_LARGE)
    )

    CompositionLocalProvider(LocalScreenSize provides screenSize) {
        MaterialTheme(
            content = content,
            colors = colorPalette.colors,
            typography = WeatherTheme.createTypography(isLargeScreen)
        )
    }
}
