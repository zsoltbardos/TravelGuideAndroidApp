package com.example.androidtravelapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.androidtravelapp.data.auth.AuthState
import com.example.androidtravelapp.navigation.AppDestination
import com.example.androidtravelapp.navigation.AppNavigation
import com.example.androidtravelapp.ui.theme.AndroidTravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var currentLocale by mutableStateOf(Locale.getDefault())
    
    @Inject
    lateinit var authState: AuthState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        try {
            setContent {
    AndroidTravelAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation(
                startDestination = AppDestination.Login.route,
                currentLocale = currentLocale,
                onLocaleChanged = { locale ->
    setLocale(this, locale)
    currentLocale = locale
    recreate() // Force activity refresh after language change
},
                authState = authState
            )
        }
    }
}
        } catch (e: Exception) {
            // Log the exception
            Log.e("MainActivity", "Error during app initialization", e)
            // Show a toast to help with debugging
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    companion object {
        /**
         * Changes the app's locale
         */
        fun setLocale(context: Context, locale: Locale) {
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = Configuration(resources.configuration)
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}