package com.example.playimdb.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
private val AppColorScheme = darkColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorOnPrimary,
    background = ColorBackground,
    surface = ColorSurface,
    surfaceVariant = ColorSurfaceVariant,
    onBackground = ColorOnBackground,
    onSurface = ColorOnSurface,
    secondary = ColorSecondary,
    onSecondary = ColorOnBackground,
    error = ColorError
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayIMDBTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
