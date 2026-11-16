package com.example.tusequipmentbooking.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShell(
    selectedRoute: String?,
    enabled: Boolean,
    onWelcome: () -> Unit,
    onBrowseEquipment: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = enabled,
        drawerContent = {
            ModalDrawerSheet {
                Text("TUS Equipment Booking", modifier = Modifier.padding(16.dp))

                NavigationDrawerItem(
                    label = { Text("Welcome") },
                    selected = selectedRoute == "welcome",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = {
                        if (enabled) {
                            scope.launch {
                                drawerState.close()
                                onWelcome()
                            }
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Browse equipment") },
                    selected = selectedRoute == "equipment",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = {
                        if (enabled) {
                            scope.launch {
                                drawerState.close()
                                onBrowseEquipment()
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TUS Equipment Booking") },
                    navigationIcon = {
                        IconButton(
                            enabled = enabled,
                            modifier = Modifier.semantics {
                                contentDescription = "Open navigation drawer"
                            },
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) { Text("☰") }
                    }
                )
            }
        ) { padding ->
            content(padding)
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }
}
