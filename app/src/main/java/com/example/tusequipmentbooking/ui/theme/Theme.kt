package com.example.tusequipmentbooking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public val tusColors = lightColorScheme(
    primary = TusGold,
    onPrimary = TusWhite,
    primaryContainer = TusLightGrey,
    secondary = TusDarkGrey,
    background = TusBackground,
    surface = TusWhite,
    onPrimaryContainer = Color.Black,
    secondaryContainer = TusLightGrey,
    surfaceVariant = TusWhite,
    onSurfaceVariant = TusDarkGrey,
    surfaceContainer = TusWhite,
    surfaceContainerLow = TusWhite
)

private val tusDarkColors = darkColorScheme(
    primary = TusGold,
    onPrimary = TusWhite,
    primaryContainer = TusDarkGrey,
    onPrimaryContainer = TusWhite,
    secondary = TusLightGrey,
    secondaryContainer = TusDarkGrey,
    background = TusDarkBackground,
    surface = TusDarkSurface,
    surfaceVariant = TusDarkSurface,
    onSurfaceVariant = TusLightGrey,
    surfaceContainer = TusDarkSurface,
    surfaceContainerLow = TusDarkSurface
)

@Composable
fun TUSEquipmentBookingTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) tusDarkColors else tusColors,
        content = content
    )
}
