package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapplicationdevelopment2025.navigation.AppNavGraph
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetConfirmationPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun getConfirmationPage_elementsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                GetConfirmationPage(navController = rememberNavController())
            }
        }

        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("send_code_button").assertExists()
        composeTestRule.onNodeWithTag("create_account_button").assertExists()
        composeTestRule.onNodeWithTag("back_to_login_button").assertExists()
    }

    @Test
    fun getConfirmationPage_canTypeEmail() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                GetConfirmationPage(navController = rememberNavController())
            }
        }

        composeTestRule.onNodeWithTag("email_input").performTextInput("luba@example.com")
    }

    @Test
    fun getConfirmationPage_canClickButtons() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MobileApplicationDevelopment2025Theme {
                AppNavGraph(
                    navController = navController,
                    startDestination = "login" // 👈 start from login screen
                )
            }

            // Navigate to get_confirmation manually
            LaunchedEffect(Unit) {
                navController.navigate("get_confirmation")
            }
        }

        // Wait for the screen to settle
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("email_input").fetchSemanticsNodes().isNotEmpty()
        }

        // Now you can safely click buttons
        composeTestRule.onNodeWithTag("send_code_button").performClick()
        composeTestRule.onNodeWithTag("create_account_button").performClick()
    }




}
