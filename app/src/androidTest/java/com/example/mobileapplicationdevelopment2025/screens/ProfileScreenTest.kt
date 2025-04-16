package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_elementsAreDisplayed() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Check for Instagram logo
        composeTestRule.onNodeWithTag("instagram_logo").assertExists()

        // Check for profile picture
        composeTestRule.onNodeWithTag("profile_picture").assertExists()

        // Check for stats section
        composeTestRule.onNodeWithText("Posts").assertExists()
        composeTestRule.onNodeWithText("Followers").assertExists()
        composeTestRule.onNodeWithText("Following").assertExists()

        // Check for action buttons
        composeTestRule.onNodeWithText("Edit Profile").assertExists()
        composeTestRule.onNodeWithText("Log Out").assertExists()
    }

    @Test
    fun profileScreen_canClickEditProfile() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Click edit profile button
        composeTestRule.onNodeWithText("Edit Profile").performClick()
    }

    @Test
    fun profileScreen_canClickLogOut() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Click log out button
        composeTestRule.onNodeWithText("Log Out").performClick()
    }

    @Test
    fun profileScreen_displaysUserInfo() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Check for username display in the top bar
        composeTestRule.onNodeWithText("@Loading...", substring = true)
            .assertExists()
            .assertIsDisplayed()

        // Check for name and username fields (they should exist even if empty)
        composeTestRule.onNodeWithTag("profile_name").assertExists()
        composeTestRule.onNodeWithTag("profile_username").assertExists()
    }

    @Test
    fun profileScreen_displaysPostsSection() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Check that either the posts grid or the "No posts yet" message exists
        val postsGridExists = try {
            composeTestRule.onNodeWithTag("posts_grid").assertExists()
            true
        } catch (e: AssertionError) {
            false
        }

        val noPostsMessageExists = try {
            composeTestRule.onNodeWithText("No posts yet").assertExists()
            true
        } catch (e: AssertionError) {
            false
        }

        // Assert that at least one of them exists
        assert(postsGridExists || noPostsMessageExists) {
            "Neither posts grid nor 'No posts yet' message was found"
        }
    }

    @Test
    fun profileScreen_displaysFollowButton() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("profile") {
                        // Pass a non-null userId to show another user's profile
                        ProfileScreen(navController = navController, userId = "test_user_id")
                    }
                    composable("edit_profile") {
                        // Empty composable for testing
                    }
                    composable("login") {
                        // Empty composable for testing
                    }
                }
            }
        }

        // Check that either "Follow" or "Unfollow" button exists and is clickable
        try {
            composeTestRule.onNodeWithText("Follow")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
        } catch (e: AssertionError) {
            composeTestRule.onNodeWithText("Unfollow")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }
} 