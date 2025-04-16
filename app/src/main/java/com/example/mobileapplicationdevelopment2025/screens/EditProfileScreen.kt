package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.viewmodel.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val currentUser by viewModel.currentUser.collectAsState()
    
    // Initialize state with current user data
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var link1 by remember { mutableStateOf("") }
    var link2 by remember { mutableStateOf("") }
    var link3 by remember { mutableStateOf("") }

    // Update state when currentUser changes
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            firstName = user.first_name ?: ""
            lastName = user.last_name ?: ""
            username = user.username ?: ""
            bio = user.bio ?: ""
            
            // Initialize links
            user.links?.let { links ->
                // Log the links for debugging
                Log.d("EditProfileScreen", "Links: $links")
                
                // Get the first three links if they exist
                val linkEntries = links.entries.toList()
                if (linkEntries.isNotEmpty()) {
                    link1 = linkEntries[0].value
                    if (linkEntries.size > 1) link2 = linkEntries[1].value
                    if (linkEntries.size > 2) link3 = linkEntries[2].value
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // First Name
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Last Name
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Links Section
            Text(
                text = "Links (up to 3)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Link 1
            OutlinedTextField(
                value = link1,
                onValueChange = { link1 = it },
                label = { Text("Link 1") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            // Link 2
            OutlinedTextField(
                value = link2,
                onValueChange = { link2 = it },
                label = { Text("Link 2") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            // Link 3
            OutlinedTextField(
                value = link3,
                onValueChange = { link3 = it },
                label = { Text("Link 3") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    // Create a map of links
                    val links = mutableMapOf<String, String>()
                    if (link1.isNotBlank()) links["Link 1"] = link1
                    if (link2.isNotBlank()) links["Link 2"] = link2
                    if (link3.isNotBlank()) links["Link 3"] = link3

                    // Update user profile
                    currentUser?.let { user ->
                        val updatedUser = user.copy(
                            first_name = firstName,
                            last_name = lastName,
                            username = username,
                            bio = bio,
                            links = links
                        )
                        viewModel.updateUserProfile(updatedUser)
                        // Navigate back to profile screen
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
} 