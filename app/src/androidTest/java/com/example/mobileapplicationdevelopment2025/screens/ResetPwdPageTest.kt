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
class ResetPwdPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun resetPwdPage_elementsAreVisible() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                ResetPwdPage(
                    navController = rememberNavController(),
                    email = "test@email.com",
                    code = "123456"
                )
            }
        }

        composeTestRule.onNodeWithTag("new_password_input").assertExists()
        composeTestRule.onNodeWithTag("confirm_password_input").assertExists()
        composeTestRule.onNodeWithTag("submit_reset_button").assertExists()
        composeTestRule.onNodeWithTag("cancel_button").assertExists()
    }

    @Test
    fun resetPwdPage_canTypeIntoFields() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                ResetPwdPage(
                    navController = rememberNavController(),
                    email = "test@email.com",
                    code = "123456"
                )
            }
        }

        composeTestRule.onNodeWithTag("new_password_input").performTextInput("newpass123")
        composeTestRule.onNodeWithTag("confirm_password_input").performTextInput("newpass123")
    }

    @Test
    fun resetPwdPage_canClickResetButton() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                ResetPwdPage(
                    navController = rememberNavController(),
                    email = "test@email.com",
                    code = "123456"
                )
            }
        }

        composeTestRule.onNodeWithTag("submit_reset_button").performClick()
    }
}
