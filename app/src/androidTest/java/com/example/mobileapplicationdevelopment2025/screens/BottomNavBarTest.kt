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
class BottomNavBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNavBar_iconsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                BottomNavBar(navController = rememberNavController())
            }
        }

        // Check if all nav icons exist by contentDescription
        composeTestRule.onNodeWithContentDescription("feed").assertExists()
        composeTestRule.onNodeWithContentDescription("search").assertExists()
        composeTestRule.onNodeWithContentDescription("new_post").assertExists()
        composeTestRule.onNodeWithContentDescription("profile").assertExists()
    }
    @Test
    fun bottomNavBar_iconsAreClickable() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController, startDestination = "feed")
            }
        }

        // Now only one node per icon will be found
        composeTestRule.onNodeWithContentDescription("feed").performClick()
        composeTestRule.onNodeWithContentDescription("search").performClick()
        composeTestRule.onNodeWithContentDescription("new_post").performClick()
        composeTestRule.onNodeWithContentDescription("profile").performClick()
    }


}
