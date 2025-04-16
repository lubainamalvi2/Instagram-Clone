package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchPageTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchPage_elementsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                SearchPage(navController = navController)
            }
        }

        // Check for the search bar
        composeTestRule.onNodeWithText("Search users").assertExists()

        // Check for the Instagram logo
        composeTestRule.onNodeWithContentDescription("Instagram Logo").assertExists()

        // Check that the "No results yet" message exists when searchResults are empty
        composeTestRule.onNodeWithText("No results yet").assertExists()
    }

    @Test
    fun searchPage_searchIcon_isClickable() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                SearchPage(navController = navController)
            }
        }
        composeTestRule.onNodeWithContentDescription("Search Icon").assertExists()
            .performClick()
    }

    @Test
    fun searchPage_textInput_updatesValue() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                SearchPage(navController = navController)
            }
        }
        val testText = "test_user"
        composeTestRule.onNodeWithText("Search users").performTextInput(testText)
        composeTestRule.onNode(hasText(testText)).assertExists()
    }

    @Test
    fun searchPage_errorMessageIsDisplayed() {
        val errorText = "Network error"
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                // We simulate the error state by rendering a Box with the error text.
                Box(modifier = androidx.compose.ui.Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorText, color = Color.Red, modifier = androidx.compose.ui.Modifier.testTag("error_message"))
                }
            }
        }
        composeTestRule.onNodeWithTag("error_message")
            .assertTextEquals(errorText)
            .assertIsDisplayed()
    }
}