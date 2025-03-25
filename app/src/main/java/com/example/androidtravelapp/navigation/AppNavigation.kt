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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtravelapp.data.auth.AuthState
import com.example.androidtravelapp.ui.auth.LoginScreen
import com.example.androidtravelapp.ui.auth.RegisterScreen
import com.example.androidtravelapp.ui.components.AppDrawer
import com.example.androidtravelapp.ui.details.DestinationDetailsScreen
import com.example.androidtravelapp.ui.home.HomeScreen
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Navigation destinations for the app
 */
sealed class AppDestination(val route: String) {
    object Login : AppDestination("login")
    object Register : AppDestination("register")
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
    startDestination: String = AppDestination.Login.route,
    modifier: Modifier = Modifier,
    currentLocale: Locale = Locale.getDefault(),
    onLocaleChanged: (Locale) -> Unit = {},
    authState: AuthState
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var shouldOpenDrawer by remember { mutableStateOf(false) }
    var shouldCloseDrawer by remember { mutableStateOf(false) }
    
    val isLoggedIn by authState.isLoggedIn.collectAsState()
    val scope = rememberCoroutineScope()
    
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
    
    // Handle authentication state changes
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            // Clear the entire navigation stack and navigate to login
            navController.navigate(AppDestination.Login.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        } else {
            // When logging in, ensure we're on the home screen
            if (navController.currentDestination?.route != AppDestination.Home.route) {
                navController.navigate(AppDestination.Home.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Navigation will be handled by the LaunchedEffect above
                },
                onRegisterClick = {
                    navController.navigate(AppDestination.Register.route)
                }
            )
        }
        
        composable(route = AppDestination.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navigation will be handled by the LaunchedEffect above
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Protected routes
        composable(route = AppDestination.Home.route) {
            if (isLoggedIn) {
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
                    },
                    onLogout = {
                        scope.launch {
                            authState.logout()
                            // Navigation will be handled by the LaunchedEffect above
                        }
                    }
                ) {
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
            }
        }
        
        composable(
            route = AppDestination.DestinationDetails.route,
            arguments = listOf(
                navArgument("destinationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            if (isLoggedIn) {
                val destinationId = backStackEntry.arguments?.getInt("destinationId") ?: -1
                
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
                    },
                    onLogout = {
                        scope.launch {
                            authState.logout()
                            // Navigation will be handled by the LaunchedEffect above
                        }
                    }
                ) {
                    DestinationDetailsScreen(
                        destinationId = destinationId,
                        onBackClick = { navController.popBackStack() },
                        onMenuClick = { shouldOpenDrawer = true },
                        currentLocale = currentLocale
                    )
                }
            }
        }
        
        composable(route = AppDestination.Settings.route) {
            if (isLoggedIn) {
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
                    },
                    onLogout = {
                        scope.launch {
                            authState.logout()
                            // Navigation will be handled by the LaunchedEffect above
                        }
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}