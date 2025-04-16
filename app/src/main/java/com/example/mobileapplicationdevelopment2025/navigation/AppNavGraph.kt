package com.example.mobileapplicationdevelopment2025.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.mobileapplicationdevelopment2025.screens.*
import com.example.mobileapplicationdevelopment2025.viewmodel.CreatePostViewModel

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginPage(navController) }
        composable("get_confirmation") { GetConfirmationPage(navController) }
        composable(
            route = "enter_confirmation?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EnterConfirmationPage(navController, email)
        }
        composable(
            route = "reset_password?email={email}&code={code}",
            arguments = listOf(
                navArgument("email") { defaultValue = "" },
                navArgument("code") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            ResetPwdPage(navController, email, code)
        }
        composable("new_account") { NewAccountPage(navController) }

        composable("feed") { FeedPage(navController) }

        composable("search") { SearchPage(navController) }

        composable("new_post") { CreatePostPage(navController) }
        composable("camera") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("new_post")
            }
            val viewModel: CreatePostViewModel = viewModel(parentEntry)
            CameraCaptureScreen(navController = navController, viewModel = viewModel)
        }
        composable("gallery") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("new_post")
            }
            val viewModel: CreatePostViewModel = viewModel(parentEntry)
            GalleryPickerScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = "profile?userId={userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        )
        { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(navController = navController, userId = userId)
        }
        composable(
            route = "profileDetail?userId={userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            FeedPage(navController = navController, userId = userId)
        }
        composable("edit_profile") { EditProfileScreen(navController) }
    }
}

