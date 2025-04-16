package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import org.junit.Rule
import org.junit.Test

class FeedPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun feedPage_displaysInstagramLogo() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "feed") {
                    composable("feed") {
                        FeedPage(navController = navController)
                    }
                }
            }
        }
        composeTestRule.onNodeWithTag("insta_logo")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun feedPage_showsNoPostsMessageWhenNoPostsExist() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "feed") {
                    composable("feed") { FeedPage(navController = navController) }
                }
            }
        }
        composeTestRule.onNodeWithText("No posts available")
            .assertExists()
            .assertIsDisplayed()
    }
}
