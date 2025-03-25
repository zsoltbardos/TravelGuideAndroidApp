package com.example.androidtravelapp.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtravelapp.ui.components.AppDrawer
import com.example.androidtravelapp.ui.details.DestinationDetailsScreen
import com.example.androidtravelapp.ui.home.HomeScreen
import java.util.Locale

/**
 * Navigation destinations for the app
 */
sealed class AppDestination(val route: String) {
    object Home : AppDestination("home")
    object DestinationDetails : AppDestination("destination/{destinationId}") {
        fun createRoute(destinationId: Int): String = "destination/$destinationId"
    }
    object Settings : AppDestination("settings")
}

/**
 * App navigation system
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Home.route,
    modifier: Modifier = Modifier,
    currentLocale: Locale = Locale.getDefault(),
    onLocaleChanged: (Locale) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var shouldOpenDrawer by remember { mutableStateOf(false) }
    var shouldCloseDrawer by remember { mutableStateOf(false) }
    
    // Handle drawer open
    LaunchedEffect(shouldOpenDrawer) {
        if (shouldOpenDrawer) {
            drawerState.open()
            shouldOpenDrawer = false
        }
    }
    
    // Handle drawer close
    LaunchedEffect(shouldCloseDrawer) {
        if (shouldCloseDrawer) {
            drawerState.close()
            shouldCloseDrawer = false
        }
    }
    
    AppDrawer(
        drawerState = drawerState,
        onClose = { shouldCloseDrawer = true },
        onDestinationSelected = { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            shouldCloseDrawer = true
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.fillMaxSize()
        ) {
            composable(route = AppDestination.Home.route) {
                HomeScreen(
                    onDestinationClick = { destinationId ->
                        navController.navigate(
                            AppDestination.DestinationDetails.createRoute(destinationId)
                        )
                    },
                    onLanguageChanged = onLocaleChanged,
                    onMenuClick = { shouldOpenDrawer = true },
                    currentLocale = currentLocale
                )
            }
            
            composable(
                route = AppDestination.DestinationDetails.route,
                arguments = listOf(
                    navArgument("destinationId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val destinationId = backStackEntry.arguments?.getInt("destinationId") ?: -1
                
                DestinationDetailsScreen(
                    destinationId = destinationId,
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = { shouldOpenDrawer = true },
                    currentLocale = currentLocale
                )
            }
            
            composable(route = AppDestination.Settings.route) {
                // We'll implement the settings screen later
                Text("Settings Screen")
            }
        }
    }
}