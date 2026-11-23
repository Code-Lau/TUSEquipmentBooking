package com.example.tusequipmentbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.tusequipmentbooking.ui.theme.TUSEquipmentBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferences = remember {
                applicationContext.getSharedPreferences(
                    "equipmentbooking_settings", MODE_PRIVATE
                )
            }
            val systemDark = isSystemInDarkTheme()
            var darkTheme by rememberSaveable {
                mutableStateOf(
                    preferences.getBoolean("dark_mode", systemDark)
                )
            }

            TUSEquipmentBookingTheme(darkTheme = darkTheme) {
                EquipmentBookingApp(
                    darkTheme = darkTheme,
                    onDarkThemeChange = { enabled ->
                        darkTheme = enabled
                        preferences.edit()
                            .putBoolean("dark_mode", enabled)
                            .apply()
                    }
                )
            }
        }
    }
}
