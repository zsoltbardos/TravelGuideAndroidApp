package com.example.androidtravelapp.util

/**
 * Contains a placeholder idling resource implementation for production code.
 * The actual implementation is provided in the test source set.
 */
object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    
    // No-op implementation for production
    fun increment() {
        // No-op in production
    }
    
    fun decrement() {
        // No-op in production
    }
    
    inline fun <T> wrapEspressoIdlingResource(crossinline function: () -> T): T {
        // No-op in production, just call the function
        return function()
    }
} 