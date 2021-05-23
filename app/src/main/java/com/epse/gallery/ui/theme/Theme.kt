package com.epse.gallery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val DarkColorPalette = darkColors(
    primary = Indigo,
    primaryVariant = LightBlue,
    onPrimary = White,

    secondary = DarkRed,
    onSecondary = White,

    surface = Black,
    onSurface = White,

    background = Black,
    onBackground = White
)

private val LightColorPalette = lightColors(
    primary = Cyan,
    primaryVariant = Blue,
    onPrimary = White,

    secondary = Amber,
    onSecondary = White,

    surface = White,
    onSurface = Black,

    background = White,
    onBackground = Black,
)

@Composable
fun GalleryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}