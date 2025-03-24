package com.example.androidtravelapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidtravelapp.navigation.AppNavigation
import com.example.androidtravelapp.ui.theme.AndroidTravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        try {
            setContent {
                AndroidTravelAppTheme {
                    AppNavigation()
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