package com.example.tusequipmentbooking

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tusequipmentbooking.ui.EquipmentBookingViewModel
import com.example.tusequipmentbooking.ui.screens.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tusequipmentbooking.ui.components.MenuShell

@Composable
fun EquipmentBookingApp() {
    val application = LocalContext.current.applicationContext as Application
    val model: EquipmentBookingViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )
    val state by model.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route

    fun openWelcome() {
        navController.navigate("welcome") {
            popUpTo("welcome") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openEquipment() {
        navController.navigate("equipment") {
            popUpTo("welcome") { inclusive = false }
            launchSingleTop = true
        }
    }

    MenuShell(
        selectedRoute = selectedRoute,
        enabled = !state.isLoading && state.loadError == null,
        onWelcome = { openWelcome() },
        onBrowseEquipment = { openEquipment() }
    ) { padding ->
        if (state.isLoading) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                CircularProgressIndicator()
                Text("Loading saved equipment…")
            }
        } else if (state.loadError != null) {
            Text(
                text = "Database error: ${state.loadError}",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        } else {
            NavHost(
                navController = navController,
                startDestination = "welcome",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    HomeScreen(
                        equipment = state.equipment,
                        categories = state.categories,
                        onEquipmentOpen = { id -> navController.navigate("details/$id") },
                        onExplore = { openEquipment() }
                    )
                }

                composable("welcome") {
                    WelcomeScreen(
                        onBrowseEquipment = {
                            openEquipment()
                        },
                        onBrowseAbout = {
                            navController.navigate("about")
                        }
                    )
                }

                composable("about"){
                    AboutScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("equipment") {
                    Column(Modifier.fillMaxSize()) {
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
                        ) {
                            Text("Back")
                        }
                        Button(
                            onClick = model::addTestEquipment,
                            enabled = !state.isAdding && state.categories.isNotEmpty(),
                            modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp)
                        ) {
                            Text(if (state.isAdding) "Saving…" else "Add test equipment")
                        }
                        state.actionMessage?.let { message ->
                            Text(message, Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp))
                        }
                        Box(Modifier.weight(1f)) {
                            EquipmentScreen(
                                equipment = state.equipment,
                                categories = state.categories,
                                onEquipmentOpen = { id ->
                                    navController.navigate("details/$id")
                                }
                            )
                        }
                    }
                }

                composable(
                    route = "details/{equipmentId}",
                    arguments = listOf(navArgument("equipmentId") {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val id = entry.arguments?.getInt("equipmentId")
                    val equipment = state.equipment.firstOrNull { it.id == id }
                    if (equipment == null) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Equipment not found")
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Back")
                            }
                        }
                    } else {
                        val categoryName = state.categories.firstOrNull {
                            it.id == equipment.categoryId
                        }?.name ?: "Uncategorised"
                        EquipmentDetailsScreen(
                            equipment = equipment,
                            categoryName = categoryName,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
