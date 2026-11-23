package com.example.tusequipmentbooking.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentBookingMenuShell(
    selectedRoute: String?,
    enabled: Boolean,
    onWelcome: () -> Unit,
    onBrowseEquipment: () -> Unit,
    onSettings: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val destinations = listOf(
        Triple("welcome", "Welcome", R.drawable.ic_home),
        Triple("equipment", "Equipment", R.drawable.ic_equipments),
        Triple("settings", "Settings", R.drawable.ic_settings)
    )

    fun openDestination(route: String) {
        when (route) {
            "welcome" -> onWelcome()
            "equipment" -> onBrowseEquipment()
            "settings" -> onSettings()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = enabled,
        drawerContent = {
            ModalDrawerSheet {
                Text("TUS Equipment Booking", modifier = Modifier.padding(16.dp))
                destinations.forEach { (route, label, icon) ->
                    NavigationDrawerItem(
                        label = { Text(label) },
                        icon = {
                            Icon(painterResource(icon), contentDescription = null)
                        },
                        selected = selectedRoute == route,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        onClick = {
                            if (enabled) {
                                scope.launch {
                                    drawerState.close()
                                    openDestination(route)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TUS Equipment Booking") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(
                            enabled = enabled,
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_menu),
                                contentDescription = "Open navigation drawer"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            enabled = enabled,
                            onClick = onSettings
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_settings),
                                contentDescription = "Open settings"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (enabled && destinations.any { it.first == selectedRoute }) {
                    NavigationBar {
                        destinations.forEach { (route, label, icon) ->
                            NavigationBarItem(
                                selected = selectedRoute == route,
                                onClick = { openDestination(route) },
                                icon = {
                                    Icon(painterResource(icon), contentDescription = null)
                                },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            content(padding)
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }
}