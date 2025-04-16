package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.R
import com.example.mobileapplicationdevelopment2025.room.UserDatabase
import com.example.mobileapplicationdevelopment2025.viewmodel.FeedViewModel

@Composable
fun FeedPage(
    navController: NavController,
    userId: String? = null,
    feedViewModel: FeedViewModel = viewModel()
) {
    val context = LocalContext.current
    var currentUserId by remember { mutableStateOf("") }
    var currentUsername by remember { mutableStateOf("") }

    if (userId != null) {
        LaunchedEffect(userId) {
            currentUserId = userId
            feedViewModel.fetchFeedPosts(userId, feedType = "profile")
        }
    } else {
        LaunchedEffect(Unit) {
            val user = UserDatabase.getDatabase(context).userDao().getCurrentUser()
            if (user != null) {
                currentUserId = user.id
                currentUsername = user.username
            } else {
                println("FeedPage: No current user retrieved!")
            }
            if (currentUserId.isNotEmpty()) {
                feedViewModel.fetchFeedPosts(currentUserId, feedType = "home")
            }
        }
    }

    val feedState by feedViewModel.feedState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.instagram_black),
                contentDescription = "Instagram Logo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
                    .testTag("insta_logo")
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    feedState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    feedState.error != null -> {
                        Text(
                            text = feedState.error ?: "An error occurred",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    feedState.posts.isEmpty() -> {
                        Text(
                            text = "No posts available",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp)
                                .testTag("feed_list"),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(feedState.posts) { post ->
                                PostPage(
                                    post = post,
                                    navController = navController,
                                    currentUserId = currentUserId,
                                    initialIsLiked = post.isLiked,
                                    onLikeChanged = { isLiked, likesCount ->
                                        feedViewModel.updateLikeForPost(
                                            post.id,
                                            isLiked,
                                            currentUserId
                                        ) { newIsLiked, newLikesCount ->
                                        }
                                    },
                                    onCommentAdded = { postId, commentText ->
                                        feedViewModel.addCommentToPost(
                                            postId,
                                            currentUserId,
                                            commentText
                                        ) { success ->
                                            if (success) {
                                                println("Comment added successfully for post $postId")
                                            } else {
                                                println("Failed to add comment for post $postId")
                                            }
                                        }
                                    },
                                    currentUsername = currentUsername
                                )
                            }
                        }
                        println("FeedPage: Displaying ${feedState.posts.size} posts")
                    }
                }
            }
        }
    }
}




