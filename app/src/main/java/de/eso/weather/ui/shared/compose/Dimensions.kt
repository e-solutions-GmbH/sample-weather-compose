package de.eso.weather.ui.shared.compose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimensions(
    val titleBarHeight: Dp,

    val tileSizeLarge: Dp,
    val tileSize: Dp,
    val TileSizeSmall: Dp,

    val decoratorSize: Dp,

    // Padding
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
    val captionTextSize: TextUnit,

    val headerTextSizeLarge: TextUnit,
    val titleTextSizeLarge: TextUnit,
    val subTitleTextSizeLarge: TextUnit,
    val body1TextSizeLarge: TextUnit,
    val body2TextSizeLarge: TextUnit,
    val buttonTextSizeLarge: TextUnit,
    val captionTextSizeLarge: TextUnit
) {
    companion object {
        val Phone = Dimensions(
            titleBarHeight = 56.dp,

            tileSizeLarge = 300.dp,
            tileSize = 200.dp,
            TileSizeSmall = 150.dp,

            decoratorSize = 300.dp,

            // Padding
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
            captionTextSize = 18.sp,

            headerTextSizeLarge = 50.sp,
            titleTextSizeLarge = 40.sp,
            subTitleTextSizeLarge = 34.sp,
            body1TextSizeLarge = 28.sp,
            body2TextSizeLarge = 26.sp,
            buttonTextSizeLarge = 28.sp,
            captionTextSizeLarge = 26.sp
        )

        val Automotive: Dimensions = Phone.copy(
            titleBarHeight = 100.dp
        )
    }
}