package com.example.newsappfinalassignment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(

    primary = Yellow,
    primaryVariant = LightGreen,
    secondary = DarkBlue,
    surface = DarkGreen,
    background = Color.Black,
    onSurface = Color.White,
    onBackground = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(

    primary = LightGreen,
    primaryVariant = Green,
    secondary = Blue,
    surface = White,
    background = Color.White,
    onSurface = Color.Black,
    onBackground = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

@Composable
fun NewsAppfinalAssignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    var colors = LightColorPalette
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        colors = DarkColorPalette
        systemUiController.setSystemBarsColor(
            Color.Transparent
        )
    } else {
        systemUiController.setSystemBarsColor(
            Color.White
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}