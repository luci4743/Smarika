package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Emerald700,
    onPrimary = Color.White,
    primaryContainer = Emerald100,
    onPrimaryContainer = Emerald900,
    secondary = Amber600,
    onSecondary = Color.White,
    secondaryContainer = Amber200,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Teal700,
    onTertiary = Color.White,
    background = CanvasCream,
    onBackground = TextDark,
    surface = SurfaceCard,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF5F3EF),
    onSurfaceVariant = TextMuted,
    outline = BorderSubtle
)

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    onPrimary = Color.Black,
    primaryContainer = HighContrastSurface,
    onPrimaryContainer = HighContrastYellow,
    secondary = HighContrastWhite,
    onSecondary = Color.Black,
    background = HighContrastBg,
    onBackground = HighContrastYellow,
    surface = HighContrastSurface,
    onSurface = HighContrastWhite,
    outline = HighContrastYellow
)

@Composable
fun SmritiNERTheme(
    highContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (highContrast) HighContrastColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    SmritiNERTheme(highContrast = false, content = content)
}
