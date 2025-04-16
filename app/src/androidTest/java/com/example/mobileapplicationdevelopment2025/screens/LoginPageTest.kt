package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginPage_elementsAreDisplayed() {
        println("Running test: loginPage_elementsAreDisplayed")
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                LoginPage(navController = rememberNavController())
            }
        }

        // Check text input fields by testTag
        composeTestRule.onNodeWithTag("username_input").assertExists()
        composeTestRule.onNodeWithTag("password_input").assertExists()

        // Check login button by testTag
        composeTestRule.onNodeWithTag("login_button").assertExists()

        // Check for text elements instead of testTags for these
        composeTestRule.onNodeWithText("Forgot password?").assertExists()
        composeTestRule.onNodeWithText("Don't have an account? Sign up.").assertExists()
    }

    @Test
    fun loginPage_canTypeIntoInputs() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                LoginPage(navController = rememberNavController())
            }
        }

        // Type into each field
        composeTestRule.onNodeWithTag("username_input").performTextInput("lubamalvi")
        composeTestRule.onNodeWithTag("password_input").performTextInput("securepass123")
    }

    @Test
    fun loginPage_canClickLoginButton() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                LoginPage(navController = rememberNavController())
            }
        }

        // Fill in inputs
        composeTestRule.onNodeWithTag("username_input").performTextInput("testuser")
        composeTestRule.onNodeWithTag("password_input").performTextInput("testpassword")

        // Click login
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Optionally check that some feedback text appears (if you use fake data)
        // or just assert button is still there
        composeTestRule.onNodeWithTag("login_button").assertExists()
    }

}
