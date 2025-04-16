package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationdevelopment2025.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapplicationdevelopment2025.viewmodel.ProfileViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String? = null,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val postCount by viewModel.postCount.collectAsState()
    val followerCount by viewModel.followerCount.collectAsState()
    val followingCount by viewModel.followingCount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isCurrentUser = viewModel.isCurrentUser(userId)
    val loggedInUserId = viewModel.getLoggedInUserId()

    // Reload data when screen comes into focus
    LaunchedEffect(Unit) {
        if (userId == null) {
            // This is the current user's profile
            currentUser?.id?.let { id ->
                viewModel.loadUserPosts(id)
                viewModel.refreshUserFromApi(id)
            }
        } else {
            // This is another user's profile
            viewModel.loadUserById(userId)
            viewModel.loadUserPosts(userId)
        }
    }

    // Log the current state
    LaunchedEffect(posts) {
        Log.d("ProfileScreen", "Posts updated: ${posts.size} posts")
    }

    // Load user by ID if provided or refresh current user
    LaunchedEffect(userId) {
        if (userId != null) {
            Log.d("ProfileScreen", "Loading user with ID: $userId")
            viewModel.loadUserById(userId)
            viewModel.loadUserPosts(userId)
        } else {
            // Refresh current user's data
            currentUser?.id?.let { id ->
                Log.d("ProfileScreen", "Refreshing current user data")
                viewModel.refreshUserFromApi(id)
                viewModel.loadUserPosts(id)
            }
        }
    }

    // Refresh data when screen is focused
    DisposableEffect(navController.currentBackStackEntry?.lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (userId == null) {
                    // Refresh current user's data when screen is focused
                    currentUser?.id?.let { id ->
                        Log.d("ProfileScreen", "Screen focused - refreshing current user data")
                        viewModel.refreshUserFromApi(id)

                    }
                }
            }
        }
        navController.currentBackStackEntry?.lifecycle?.addObserver(observer)

        onDispose {
            navController.currentBackStackEntry?.lifecycle?.removeObserver(observer)
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Instagram Logo
            Image(
                painter = painterResource(id = R.drawable.instagram_black),
                contentDescription = "Instagram Logo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
                    .height(48.dp)
                    .testTag("instagram_logo")
            )

            // Top Bar (Username and Options)
            TopAppBar(
                title = {
                    Text(
                        "@${currentUser?.username ?: "Loading..."}",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (isCurrentUser) {
                        // Add settings/options icon here if needed
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // Profile Header Section
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Picture
                    if (currentUser?.pfp != null) {
                        // Load profile picture from URL using Glide
                        GlideImage(
                            model = currentUser?.pfp,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                                .testTag("profile_picture"),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Default profile picture
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                                .testTag("profile_picture"),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Name and Username
                    Column {
                        Text(
                            text = "${currentUser?.first_name ?: ""} ${currentUser?.last_name ?: ""}", 
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.testTag("profile_name")
                        )
                        Text(
                            text = "@${currentUser?.username ?: ""}", 
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.testTag("profile_username")

                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat(
                            count = postCount,
                            label = "Posts"
                        )
                        ProfileStat(
                            count = followerCount,
                            label = "Followers"
                        )
                        ProfileStat(
                            count = followingCount,
                            label = "Following"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bio
                if (currentUser?.bio != null) {
                    Text(
                        text = currentUser?.bio ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Links
                if (currentUser?.links != null && currentUser?.links?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        currentUser?.links?.forEach { (platform, url) ->
                            val context = LocalContext.current
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = "Link",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = url,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons (Edit Profile / Follow / Message)
                if (isCurrentUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate("edit_profile") },
                            modifier = Modifier.weight(1f).height(35.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Edit Profile", fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.logout()
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.height(35.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Log Out", fontSize = 13.sp)
                        }
                    }
                } else {
                    // Buttons for viewing other users' profiles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                currentUser?.id?.let { targetId ->
                                    loggedInUserId?.let { currentId ->
                                        if (currentUser?.isBeingFollowedBy(currentId) == true) {
                                            viewModel.unfollowUser(targetId)
                                        } else {
                                            viewModel.followUser(targetId)
                                        }
                                    }
                                }
                            }, 
                            modifier = Modifier
                                .weight(1f)
                                .testTag("follow_button")
                        ) {
                            Text(
                                if (currentUser?.isBeingFollowedBy(loggedInUserId ?: "") == true)
                                    "Unfollow"
                                else
                                    "Follow"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray)

            // Posts Section
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No posts yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                Log.d("ProfileScreen", "Displaying ${posts.size} posts in grid")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .testTag("posts_grid"),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    items(posts.size) { index ->
                        val post = posts[index]
                        GlideImage(
                            model = post.imageUrl,
                            contentDescription = "Post ${index + 1}",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(1.dp)
                                .clickable {
                                    navController.navigate("profileDetail?userId=${userId}")
                                }
                                .testTag("post_${index + 1}"),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}