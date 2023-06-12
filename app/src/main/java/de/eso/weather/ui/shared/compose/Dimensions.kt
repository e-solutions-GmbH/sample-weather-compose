package de.eso.weather.ui.shared.compose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimensions(
    val titleBarHeight: Dp,

    val tileSizeLarge: Dp,
    val tileSize: Dp,
    val tileSizeSmall: Dp,

    val decoratorSize: Dp,

    // Padding
    val titlePadding: Dp,
    val contentPadding: Dp,
    val containerPadding: Dp,
    val iconPadding: Dp,
    val buttonPadding: Dp,

    // Icon sizes
    val iconSizeButton: Dp,
    val iconSizeLogo: Dp,
    val iconSizeBigLogo: Dp,

    // Generic text sizes
    val headerTextSize: TextUnit,
    val titleTextSize: TextUnit,
    val subTitleTextSize: TextUnit,
    val body1TextSize: TextUnit,
    val body2TextSize: TextUnit,
    val buttonTextSize: TextUnit,
    val captionTextSize: TextUnit
) {
    companion object {
        val Phone = Dimensions(
            titleBarHeight = 56.dp,

            tileSizeLarge = 300.dp,
            tileSize = 200.dp,
            tileSizeSmall = 150.dp,

            decoratorSize = 300.dp,

            // Padding
            titlePadding = 10.dp,
            contentPadding = 16.dp,
            containerPadding = 20.dp,
            iconPadding = 20.dp,
            buttonPadding = 10.dp,

            // Icon sizes
            iconSizeButton = 60.dp,
            iconSizeLogo = 60.dp,
            iconSizeBigLogo = 80.dp,

            // Generic text sizes
            headerTextSize = 30.sp,
            titleTextSize = 24.sp,
            subTitleTextSize = 22.sp,
            body1TextSize = 20.sp,
            body2TextSize = 18.sp,
            buttonTextSize = 20.sp,
            captionTextSize = 18.sp
        )

        val Automotive: Dimensions = Phone.copy(
            titleBarHeight = 100.dp,

            tileSizeLarge = 500.dp,
            tileSize = 300.dp,
            tileSizeSmall = 150.dp,

            headerTextSize = 50.sp,
            titleTextSize = 34.sp,
            subTitleTextSize = 30.sp,
            body1TextSize = 28.sp,
            body2TextSize = 26.sp,
            buttonTextSize = 28.sp,
            captionTextSize = 26.sp
        )
    }
}