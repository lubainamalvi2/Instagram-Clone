package com.example.mobileapplicationdevelopment2025.screens

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
class EnterConfirmationPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyEmail = "test@example.com"

    @Test
    fun elementsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                EnterConfirmationPage(navController = rememberNavController(), email = dummyEmail)
            }
        }

        composeTestRule.onNodeWithTag("confirmation_code_input").assertExists()
        composeTestRule.onNodeWithTag("go_to_reset_button").assertExists()
        composeTestRule.onNodeWithTag("resend_code_button").assertExists()
        composeTestRule.onNodeWithTag("back_to_email_button").assertExists()
    }

    @Test
    fun canTypeIntoCodeField() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                EnterConfirmationPage(navController = rememberNavController(), email = dummyEmail)
            }
        }

        composeTestRule.onNodeWithTag("confirmation_code_input").performTextInput("123456")
    }

    @Test
    fun canClickButtons() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MobileApplicationDevelopment2025Theme {
                AppNavGraph(navController = navController, startDestination = "enter_confirmation?email=$dummyEmail")
            }
        }

        composeTestRule.onNodeWithTag("go_to_reset_button").performClick()
        composeTestRule.onNodeWithTag("resend_code_button").performClick()
        composeTestRule.onNodeWithTag("back_to_email_button").performClick()
    }

}
