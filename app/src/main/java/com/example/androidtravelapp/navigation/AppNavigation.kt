package com.example.androidtravelapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
}

/**
 * App navigation system
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Home.route,
    modifier: Modifier = Modifier
) {
    // Create locale state to pass to screens for localization
    val currentLocale = remember { androidx.compose.runtime.mutableStateOf(Locale.getDefault()) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = AppDestination.Home.route) {
            HomeScreen(
                onDestinationClick = { destinationId ->
                    navController.navigate(
                        AppDestination.DestinationDetails.createRoute(destinationId)
                    )
                },
                onLanguageChanged = { locale -> 
                    currentLocale.value = locale
                }
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
                currentLocale = currentLocale.value
            )
        }
    }
}