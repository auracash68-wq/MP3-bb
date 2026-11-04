package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AmoledColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = DarkTextOnCyan,
    secondary = BrightCyan,
    onSecondary = DarkTextOnCyan,
    tertiary = DeepBlue,
    background = PureBlack,
    onBackground = TextPrimary,
    surface = PrimaryCardBg,
    onSurface = TextPrimary,
    surfaceVariant = SecondaryCardBg,
    onSurfaceVariant = TextSecondary,
    outline = DividerGrey
)

@Composable
fun BlackScreenOverlayTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AmoledColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BlackScreenOverlayTheme(content = content)
}
