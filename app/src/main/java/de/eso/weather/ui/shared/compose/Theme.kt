package de.eso.weather.ui.shared.compose

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import de.eso.weather.R

object WeatherTheme {
    @Composable
    fun isLargeScreen() = LocalConfiguration.current.isLayoutSizeAtLeast(SCREENLAYOUT_SIZE_LARGE)

    fun typography(isLargeScreen: Boolean = false): Typography {
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

@Composable
fun WeatherTheme(
    isLargeScreen: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content,
        colors = Colors(
            primary = EsoColors.DarkBlue,
            primaryVariant = EsoColors.DarkestBlue,
            secondary = EsoColors.Blue,
            secondaryVariant = EsoColors.DarkerBlue,
            background = EsoColors.Black,
            surface = EsoColors.Blue,
            error = EsoColors.DarkBlue,
            onPrimary = EsoColors.White,
            onSecondary = EsoColors.White,
            onBackground = EsoColors.White,
            onSurface = EsoColors.White,
            onError = EsoColors.Red,
            isLight = false
        ),
        typography = WeatherTheme.typography(isLargeScreen)
    )
}
