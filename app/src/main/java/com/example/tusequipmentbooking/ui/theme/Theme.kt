package com.example.tusequipmentbooking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

public val tusColors = lightColorScheme(
    primary = TusGold,
    onPrimary = TusWhite,
    primaryContainer = TusLightGrey,
    secondary = TusDarkGrey,
    background = TusBackground,
    surface = TusWhite
)

@Composable
fun TUSEquipmentBookingTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = tusColors, content = content)
}
