package com.example.mobileapplicationdevelopment2025.screens

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme
import com.example.mobileapplicationdevelopment2025.viewmodel.CreatePostViewModel
import org.junit.Rule
import org.junit.Test

class TestCreatePostViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePostViewModel::class.java)) {
            return CreatePostViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CreatePostPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createPostPage_displaysUIElements() {
        composeTestRule.setContent {
            MobileApplicationDevelopment2025Theme {
                val navController = rememberNavController()
                val context = androidx.compose.ui.platform.LocalContext.current
                val viewModel: CreatePostViewModel = viewModel(
                    factory = TestCreatePostViewModelFactory(context.applicationContext as Application)
                )
                CreatePostPage(navController = navController, viewModel = viewModel)
            }
        }
        composeTestRule.onNodeWithText("New Post")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Take a photo")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Share")
            .assertExists()
            .assertIsDisplayed()
    }
}
