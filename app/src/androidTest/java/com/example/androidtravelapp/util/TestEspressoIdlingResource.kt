package com.example.androidtravelapp.util.test

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Test-only implementation of IdlingResource
 */
object TestEspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    
    val countingIdlingResource = CountingIdlingResource(RESOURCE)
    
    fun increment() {
        countingIdlingResource.increment()
    }
    
    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
    
    inline fun <T> wrapEspressoIdlingResource(crossinline function: () -> T): T {
        increment() // Set app as busy
        return try {
            function()
        } finally {
            decrement() // Set app as idle
        }
    }
} 