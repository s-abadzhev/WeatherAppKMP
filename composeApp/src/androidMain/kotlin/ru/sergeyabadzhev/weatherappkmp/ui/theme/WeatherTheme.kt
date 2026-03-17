package ru.sergeyabadzhev.weatherappkmp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WeatherColorScheme = darkColorScheme(
    primary = Color(0xFF4FC3F7),
    onPrimary = Color(0xFF003544),
    background = Color(0xFF0D3B8C),
    surface = Color(0x331A6BCC),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun WeatherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WeatherColorScheme,
        content = content
    )
}