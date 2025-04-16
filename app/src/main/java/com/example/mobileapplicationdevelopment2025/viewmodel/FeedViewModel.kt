package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.model.FeedPostsResponse
import com.example.mobileapplicationdevelopment2025.model.Post
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FeedViewModel(
    private val api: ApiService = RetrofitInstance.api
) : ViewModel() {

    private val _feedState = MutableStateFlow(FeedState())
    val feedState: StateFlow<FeedState> = _feedState

    fun fetchFeedPosts(userId: String, feedType: String = "home") {
        viewModelScope.launch {
            _feedState.value = FeedState(isLoading = true)
            try {
                val response = when (feedType) {
                    "profile" -> api.getProfileFeed(userId)
                    else -> api.getHomeFeed(userId)
                }
                if (response.isSuccessful) {
                    val feedPostsResponse: FeedPostsResponse? = response.body()
                    println("FeedViewModel: retrieved posts count = ${feedPostsResponse?.count}")
                    _feedState.value = FeedState(posts = feedPostsResponse?.posts ?: emptyList())
                } else {
                    val error = response.errorBody()?.string() ?: "Error"
                    println("FeedViewModel error: $error")
                    _feedState.value = FeedState(error = error)
                }
            } catch (e: Exception) {
                println("FeedViewModel exception: ${e.message}")
                _feedState.value = FeedState(error = e.message ?: "Exception occurred")
            }
        }
    }

    fun updateLikeForPost(
        postId: String,
        isLiked: Boolean,
        currentUserId: String,
        onResult: (Boolean, Int) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (isLiked) {
                    val response = api.likePost(postId, mapOf("user_id" to currentUserId))
                    if (response.isSuccessful) {
                        val apiResp: ApiResponse? = response.body()
                        val updatedCount = apiResp?.like_count ?: 0
                        onResult(true, updatedCount)
                    } else {
                        onResult(false, 0)
                    }
                } else {
                    val response = api.unlikePost(postId, mapOf("user_id" to currentUserId))
                    if (response.isSuccessful) {
                        val apiResp: ApiResponse? = response.body()
                        val updatedCount = apiResp?.like_count ?: 0
                        onResult(false, updatedCount)
                    } else {
                        onResult(true, 0)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(isLiked, 0)
            }
        }
    }

    fun addCommentToPost(
        postId: String,
        currentUserId: String,
        commentText: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.addComment(
                    mapOf(
                        "text" to commentText,
                        "account_id" to currentUserId,
                        "post_id" to postId
                    )
                )
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}