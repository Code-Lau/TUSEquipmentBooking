package com.example.tusequipmentbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tusequipmentbooking.ui.theme.TUSEquipmentBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TUSEquipmentBookingTheme {
                EquipmentBookingApp()
            }
        }
    }
}
