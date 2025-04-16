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
class NewAccountPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun allInputsAndButtonsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                NewAccountPage(navController = rememberNavController())
            }
        }

        // Assert inputs exist
        composeTestRule.onNodeWithTag("first_name_input").assertExists()
        composeTestRule.onNodeWithTag("last_name_input").assertExists()
        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("username_input").assertExists()
        composeTestRule.onNodeWithTag("password_input").assertExists()
        composeTestRule.onNodeWithTag("confirm_password_input").assertExists()

        // Assert buttons exist
        composeTestRule.onNodeWithTag("create_account_button").assertExists()
        composeTestRule.onNodeWithTag("already_have_account_button").assertExists()
    }

    @Test
    fun canTypeIntoTextFields() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                NewAccountPage(navController = rememberNavController())
            }
        }

        // Type into each field
        composeTestRule.onNodeWithTag("first_name_input").performTextInput("Luba")
        composeTestRule.onNodeWithTag("last_name_input").performTextInput("Malvi")
        composeTestRule.onNodeWithTag("email_input").performTextInput("luba@example.com")
        composeTestRule.onNodeWithTag("username_input").performTextInput("lubamalvi")
        composeTestRule.onNodeWithTag("password_input").performTextInput("securepassword")
        composeTestRule.onNodeWithTag("confirm_password_input").performTextInput("securepassword")
    }

    @Test
    fun createAccountButton_canBeClicked() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                NewAccountPage(navController = rememberNavController())
            }
        }

        composeTestRule.onNodeWithTag("create_account_button").performClick()
    }

    @Test
    fun alreadyHaveAccountButton_canBeClicked() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MobileApplicationDevelopment2025Theme {
                AppNavGraph(
                    navController = navController,
                    startDestination = "new_account"
                )
            }
        }
        composeTestRule.onNodeWithTag("already_have_account_button").performClick()
    }

}
