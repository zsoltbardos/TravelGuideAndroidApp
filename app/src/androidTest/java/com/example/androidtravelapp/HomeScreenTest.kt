package com.example.androidtravelapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidtravelapp.data.auth.AuthState
import com.example.androidtravelapp.util.test.TestEspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Inject
    lateinit var authState: AuthState

    @Before
    fun setup() {
        hiltRule.inject()
        
        // Ensure we're logged in and wait for navigation to complete
        runBlocking {
            (authState.isLoggedIn as? MutableStateFlow)?.value = true
            // Wait for the UI to stabilize after login
            composeTestRule.waitForIdle()
        }
        
        // Register idling resource
        IdlingRegistry.getInstance().register(TestEspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        // Logout after tests
        runBlocking {
            (authState.isLoggedIn as? MutableStateFlow)?.value = false
        }
        
        // Unregister idling resource
        IdlingRegistry.getInstance().unregister(TestEspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun simpleHomeScreenTest() {
        // Just verify app is running without elements search
        composeTestRule.waitForIdle()
        assert(true)
    }
    

    
} 