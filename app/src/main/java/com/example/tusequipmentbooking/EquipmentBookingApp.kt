package com.example.tusequipmentbooking

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tusequipmentbooking.ui.AuthViewModel
import com.example.tusequipmentbooking.ui.EquipmentBookingViewModel
import com.example.tusequipmentbooking.ui.components.EquipmentBookingMenuShell
import com.example.tusequipmentbooking.ui.screens.AboutScreen
import com.example.tusequipmentbooking.ui.screens.BookingActions
import com.example.tusequipmentbooking.ui.screens.EquipmentDetailsScreen
import com.example.tusequipmentbooking.ui.screens.HelpScreen
import com.example.tusequipmentbooking.ui.screens.HomeScreen
import com.example.tusequipmentbooking.ui.screens.LoginScreen
import com.example.tusequipmentbooking.ui.screens.ManageEquipmentScreen
import com.example.tusequipmentbooking.ui.screens.MyBookingsScreen
import com.example.tusequipmentbooking.ui.screens.RegisterScreen
import com.example.tusequipmentbooking.ui.screens.SettingsScreen
import com.example.tusequipmentbooking.ui.screens.WelcomeScreen

@Composable
fun EquipmentBookingApp(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val application = LocalContext.current.applicationContext as Application
    val model: EquipmentBookingViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )
    val state by model.uiState.collectAsStateWithLifecycle()
    val authModel: AuthViewModel = viewModel()
    val authState by authModel.uiState.collectAsStateWithLifecycle()
    val bookingState by model.bookingUiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route

    fun openTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo("welcome") { inclusive = false }
            launchSingleTop = true
        }
    }

    EquipmentBookingMenuShell(
        selectedRoute = selectedRoute,
        enabled = !state.isLoading && state.loadError == null,
        onWelcome = { openTopLevel("welcome") },
        onBrowseEquipment = { openTopLevel("equipment") },
        onSettings = { openTopLevel("settings") }
    ) { padding ->
        if (state.isLoading) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                CircularProgressIndicator()
                Text("Loading saved equipment…")
            }
        } else if (state.loadError != null) {
            Text("Database error: ${state.loadError}",
                Modifier.padding(padding).padding(16.dp))
        } else {
            NavHost(navController = navController,
                startDestination = "welcome",
                modifier = Modifier.padding(padding)) {
                composable("welcome") {
                    WelcomeScreen(
                        signedIn = authState.uid != null,
                        onBrowseEquipment = { openTopLevel("equipment") },
                        onLogin = { navController.navigate("login") },
                        onRegister = { navController.navigate("register") },
                        onBrowseAbout = { openTopLevel("about") },
                        onHelp = { openTopLevel("help") }
                    )
                }
                composable("about") {
                    AboutScreen(onBack = { navController.popBackStack() })
                }
                composable("help") {
                    HelpScreen(onBack = { navController.popBackStack() })
                }
                composable("login") {
                    LaunchedEffect(authState.uid) {
                        if (authState.uid != null) {
                            if (!navController.popBackStack()) openTopLevel("equipment")
                        }
                    }
                    LoginScreen(isBusy = authState.isBusy,
                        error = authState.error,
                        onLogin = authModel::login,
                        onCancel = { navController.popBackStack() })
                }
                composable("register") {
                    LaunchedEffect(authState.uid) {
                        if (authState.uid != null) {
                            if (!navController.popBackStack()) openTopLevel("equipment")
                        }
                    }
                    RegisterScreen(isBusy = authState.isBusy,
                        error = authState.error,
                        onRegister = authModel::register,
                        onCancel = { navController.popBackStack() })
                }
                composable("bookings") {
                    LaunchedEffect(Unit) { model.clearMessage() }
                    MyBookingsScreen(signedIn = authState.uid != null,
                        bookings = bookingState.bookings,
                        message = bookingState.message,
                        isBusy = bookingState.isBooking,
                        onCancelBooking = { model.cancelBooking(it) },
                        onLogin = { navController.navigate("login") },
                        onBack = { navController.popBackStack() },
                        onBrowseEquipment = { openTopLevel("equipment") })
                }
                composable("settings") {
                    SettingsScreen(
                        darkTheme = darkTheme,
                        onDarkThemeChange = onDarkThemeChange
                    )
                }
                composable("home") {
                    HomeScreen(
                        equipment = state.equipment,
                        categories = state.categories,
                        bookedEquipmentIds = state.bookedEquipmentIds,
                        onEquipmentOpen = { navController.navigate("details/$it") },
                        onExplore = { openTopLevel("equipment") },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("equipment") {
                    ManageEquipmentScreen(
                        equipment = state.equipment,
                        categories = state.categories,
                        bookedEquipmentIds = state.bookedEquipmentIds,
                        onRegister = { navController.navigate("register") },
                        onMyBookings = { navController.navigate("bookings") },
                        accountEmail = authState.email,
                        onLogin = { navController.navigate("login") {
                            launchSingleTop = true
                        } },
                        onLogout = {
                            authModel.logout()
                            openTopLevel("welcome")
                            navController.navigate("login")
                        },
                        onEquipmentOpen = { navController.navigate("details/$it") }
                    )
                }
                composable("details/{equipmentId}",
                    arguments = listOf(navArgument("equipmentId") {
                        type = NavType.IntType
                    })) { entry ->
                    LaunchedEffect(Unit) { model.clearMessage() }
                    val id = entry.arguments?.getInt("equipmentId")
                    val item = state.equipment.firstOrNull { it.id == id }
                    if (item == null) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Equipment not found")
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Back")
                            }
                        }
                    } else {
                        val categoryName = state.categories.firstOrNull {
                            it.id == item.categoryId
                        }?.name ?: "Uncategorised"
                        val isBooked = item.id in state.bookedEquipmentIds
                        Column(Modifier.fillMaxSize()) {
                            BookingActions(equipment = item,
                                categoryName = categoryName,
                                signedIn = authState.uid != null,
                                isBooked = isBooked,
                                isBooking = bookingState.isBooking,
                                message = bookingState.message,
                                onLogin = { navController.navigate("login") },
                                onRegister = { navController.navigate("register") },
                                onBook = { days, purpose ->
                                    model.bookEquipment(item.id, days, purpose)
                                },
                                onMyBookings = { navController.navigate("bookings") })
                            Box(Modifier.weight(1f)) {
                                EquipmentDetailsScreen(equipment = item,
                                    categoryName = categoryName,
                                    isBooked = isBooked,
                                    onBack = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}
