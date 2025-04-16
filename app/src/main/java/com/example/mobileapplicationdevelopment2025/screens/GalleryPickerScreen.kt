package com.example.mobileapplicationdevelopment2025.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.mobileapplicationdevelopment2025.viewmodel.CreatePostViewModel

@Composable
fun GalleryPickerScreen(navController: NavHostController, viewModel: CreatePostViewModel) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setSelectedImagePath(it.toString())
        }
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        galleryLauncher.launch("image/*")
    }
}
