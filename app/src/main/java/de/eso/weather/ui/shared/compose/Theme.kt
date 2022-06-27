package de.eso.weather.ui.shared.compose

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

object WeatherTheme {
    val defaultFontFamily = FontFamily.SansSerif

    val largeScreenTypography = Typography(
        defaultFontFamily = FontFamily.SansSerif,
        h4 = TextStyle(fontSize = Dimensions.HeaderTextSizeLarge),
        h6 = TextStyle(fontSize = Dimensions.TitleTextSizeLarge),
        subtitle1 = TextStyle(fontSize = Dimensions.SubTitleTextSizeLarge),
        body1 = TextStyle(fontSize = Dimensions.Body1TextSizeLarge),
        body2 = TextStyle(fontSize = Dimensions.Body2TextSizeLarge),
        button = TextStyle(fontSize = Dimensions.ButtonTextSizeLarge),
        caption = TextStyle(fontSize = Dimensions.CaptionTextSizeLarge)
    )

    @Composable
    fun isLargeScreen() = LocalConfiguration.current.isLayoutSizeAtLeast(SCREENLAYOUT_SIZE_LARGE)
}

@Composable
fun WeatherTheme(
    content: @Composable () -> Unit
) {
    // Choose fitting typography depending on screen size (e.g. larger for Automotive resolutions)
    val typography = if (WeatherTheme.isLargeScreen()) {
        WeatherTheme.largeScreenTypography
    } else {
        Typography(defaultFontFamily = WeatherTheme.defaultFontFamily)
    }

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
        typography = typography
    )
}
