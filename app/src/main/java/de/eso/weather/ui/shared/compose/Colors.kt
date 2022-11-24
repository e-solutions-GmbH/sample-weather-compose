package de.eso.weather.ui.shared.compose

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object EsoColors {
    val Orange = Color(245, 160, 0)
    val Red = Color(105, 35, 0)
    val Blue = Color(23, 50, 71)
    val LightBlue = Color(129, 190, 238, 255)
    val Violet = Color(76, 76, 111)
    val Pink = Color(190, 86, 131)
    val Green = Color(19, 111, 19)
    val DarkGreen = Color(10, 70, 10)

    val Black = Color(0, 0, 20)
    val White = Color.White
}

val DefaultColors = Colors(
    primary = EsoColors.Blue,
    primaryVariant = EsoColors.Blue,
    secondary = EsoColors.Blue,
    secondaryVariant = EsoColors.Blue,
    background = EsoColors.Black,
    surface = EsoColors.Black,
    error = EsoColors.Blue,
    onPrimary = EsoColors.White,
    onSecondary = EsoColors.White,
    onBackground = EsoColors.White,
    onSurface = EsoColors.White,
    onError = EsoColors.Red,
    isLight = false
)

object ColorPalettes {
    val LightBlue = ColorPalette(name = "Light Blue", color = EsoColors.LightBlue)
    val DarkBlue = ColorPalette(name = "Dark Blue", color = EsoColors.Blue)
    val Red = ColorPalette(name = "Red", color = EsoColors.Red)
    val Green = ColorPalette(name = "Green", color = EsoColors.Green)
    val DarkGreen = ColorPalette(name = "Dark Green", color = EsoColors.DarkGreen)
    val Violet = ColorPalette(name = "Violet", color = EsoColors.Violet)
    val Pink = ColorPalette(name = "Pink", color = EsoColors.Pink)

    val all: List<ColorPalette> = listOf(
        LightBlue,
        DarkBlue,
        Red,
        Green,
        DarkGreen,
        Violet,
        Pink
    )
}

@Immutable
data class ColorPalette(
    val name: String,
    val color: Color
) {
    val colors: Colors
        get() {
            return DefaultColors.copy(
                primary = color.dark,
                primaryVariant = color.darkest,
                secondary = color,
                secondaryVariant = color.darker,
                background = color.dark.copy(alpha = 0.3f)
            )
        }
}

val Color.darker: Color
    get() = reduce(15)

val Color.dark: Color
    get() = reduce(25)

val Color.darkest: Color
    get() = reduce(30)

fun Color.reduce(diff: Int): Color {
    val darkerRed = (red - diff / 255f).coerceAtLeast(0f)
    val darkerGreen = (green - diff / 255f).coerceAtLeast(0f)
    val darkerBlue = (blue - diff / 255f).coerceAtLeast(0f)

    return copy(red = darkerRed, green = darkerGreen, blue = darkerBlue)
}
