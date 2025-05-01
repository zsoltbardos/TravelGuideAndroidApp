package com.example.androidtravelapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidtravelapp.util.test.TestEspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        // Register idling resource to handle asynchronous operations
        IdlingRegistry.getInstance().register(TestEspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        // Unregister idling resource
        IdlingRegistry.getInstance().unregister(TestEspressoIdlingResource.countingIdlingResource)
    }

    // Helper function to wait for a node by tag
    private fun waitForNodeWithTag(tag: String, timeoutMillis: Long = 10000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            val found = composeTestRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
            if (!found) println("[DEBUG] Node with tag '$tag' not found yet.")
            found
        }
    }
    // Helper function to wait for a node by text
    private fun waitForNodeWithText(text: String, timeoutMillis: Long = 10000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            val found = composeTestRule.onAllNodesWithText(text, substring = true, ignoreCase = true).fetchSemanticsNodes().isNotEmpty()
            if (!found) println("[DEBUG] Node with text '$text' not found yet.")
            found
        }
    }
    // Helper function to wait for a node by content description
    private fun waitForNodeWithContentDescription(desc: String, timeoutMillis: Long = 10000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            val found = composeTestRule.onAllNodesWithContentDescription(desc).fetchSemanticsNodes().isNotEmpty()
            if (!found) println("[DEBUG] Node with contentDescription '$desc' not found yet.")
            found
        }
    }

    private fun printAllVisibleTexts() {
        println("[DEBUG] Printing all visible text nodes:")
        try {
            // Get all nodes that might have text
            val allNodes = composeTestRule.onAllNodes(isEnabled()).fetchSemanticsNodes()
            allNodes.forEach { node ->
                // Try to get text if available
                val textList = node.config.getOrElse(androidx.compose.ui.semantics.SemanticsProperties.Text) { emptyList() }
                if (textList.isNotEmpty()) {
                    val text = textList.joinToString(", ") { it.text }
                    println("- Text: $text")
                }
                
                // Try to get content description if available
                val contentDescription = node.config.getOrElse(androidx.compose.ui.semantics.SemanticsProperties.ContentDescription) { emptyList() }
                if (contentDescription.isNotEmpty()) {
                    println("- ContentDesc: ${contentDescription.joinToString()}")
                }
                
                // Try to get test tag if available
                val testTag = node.config.getOrElse(androidx.compose.ui.semantics.SemanticsProperties.TestTag) { "" }
                if (testTag.isNotEmpty()) {
                    println("- TestTag: $testTag")
                }
            }
            
            println("[DEBUG] Total nodes found: ${allNodes.size}")
        } catch (e: Exception) {
            println("[DEBUG] Error printing nodes: ${e.message}")
        }
    }

    @Test
    fun test1_LoginScreenIsDisplayed() {
        waitForNodeWithText("Welcome Back")
        composeTestRule.onNodeWithText("Welcome Back", substring = true, ignoreCase = true).assertIsDisplayed()
        waitForNodeWithTag("email_field")
        composeTestRule.onNodeWithTag("email_field").assertIsDisplayed()
        waitForNodeWithTag("password_field")
        composeTestRule.onNodeWithTag("password_field").assertIsDisplayed()
        waitForNodeWithText("Register")
        composeTestRule.onNodeWithText("Register", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun test2_LoginWithInvalidEmail_ShowsInvalidCredentialsMessage() {
        // Clear fields first to ensure we're starting fresh
        waitForNodeWithTag("email_field")
        composeTestRule.onNodeWithTag("email_field").performTextClearance()
        composeTestRule.onNodeWithTag("email_field").performTextInput("invalid-email")
        
        waitForNodeWithTag("password_field")
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextInput("password")
        
        // Press login and wait longer for error message
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // Increase timeout for error message to appear
        waitForNodeWithText("Invalid credentials", 15000)
        composeTestRule.onNodeWithText("Invalid credentials", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun test3_RegisterLinkNavigatesToRegisterScreen() {
        waitForNodeWithText("Welcome Back")
        waitForNodeWithText("Register")
        composeTestRule.onNodeWithText("Register", substring = true, ignoreCase = true).performClick()
        waitForNodeWithText("Create Account")
        composeTestRule.onNodeWithText("Create Account", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun test4_LoginWithValidCredentialsNavigatesToHomeScreen() {
        // Make sure we're on the login screen first
        waitForNodeWithTag("email_field")
        composeTestRule.onNodeWithTag("email_field").performTextClearance()
        composeTestRule.onNodeWithTag("email_field").performTextInput("test4@test.com")
        
        waitForNodeWithTag("password_field")
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextInput("password")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // After clicking login, wait longer for navigation
        Thread.sleep(5000)
        
        // Print what's actually on screen for debugging
        println("[DEBUG] After login - printing visible elements:")
        printAllVisibleTexts()
        
        // Check for the Open Menu button which should be visible on all screens after login
        try {
            composeTestRule.onNodeWithContentDescription("Open Menu").assertExists()
            println("[DEBUG] Found 'Open Menu' button")
        } catch (e: Exception) {
            // If specific content description fails, try with more generic checks
            waitForNodeWithContentDescription("Menu", 5000)
            println("[DEBUG] Found 'Menu' button instead")
        }
    }
} 