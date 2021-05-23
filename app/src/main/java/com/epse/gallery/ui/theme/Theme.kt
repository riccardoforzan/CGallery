package com.epse.gallery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * Some components like Button supports only colours found in android.graphics.Color
 */

private val DarkColorPalette = darkColors(
    primary = Red,
    primaryVariant = Magenta,
    onPrimary = White,

    secondary = Yellow,
    onSecondary = White,

    surface = Black,
    onSurface = White,

    background = Black,
    onBackground = White
)

private val LightColorPalette = lightColors(
    primary = Blue,
    primaryVariant = Cyan,
    onPrimary = White,

    secondary = Yellow,
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