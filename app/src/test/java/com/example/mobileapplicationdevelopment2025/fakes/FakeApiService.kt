package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.model.FeedPostsResponse
import com.example.mobileapplicationdevelopment2025.model.Post
import com.example.mobileapplicationdevelopment2025.model.SearchProfileResult
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.model.UserPostsResponse
import com.example.mobileapplicationdevelopment2025.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeApiService : ApiService {

    var simulateSuccess = true
    var fakeUserId = "user123"
    var storedUser: User? = null
    var storedPosts: List<Post> = emptyList()
    var storedFeedResponse: FeedPostsResponse = FeedPostsResponse(posts = emptyList(), count = 0)
    var likeCount: Int = 0
    var simulateNullUserBody = false

    override suspend fun registerUser(user: User): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Registered", user_id = fakeUserId, error = null))
        } else {
            Response.error(
                500,
                "Server error".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun login(credentials: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Login successful", user_id = fakeUserId, error = null))
        } else {
            Response.error(
                401,
                "Unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun sendResetCode(email: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Reset code sent", user_id = null, error = null))
        } else {
            Response.error(
                500,
                "Email not found".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun verifyResetCode(body: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Code verified", user_id = null, error = null))
        } else {
            Response.error(
                400,
                "Invalid or expired code".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun resetPassword(data: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Password reset", user_id = null, error = null))
        } else {
            Response.error(
                400,
                "Reset failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun getUserById(userId: String): Response<User> {
        return if (simulateSuccess && simulateNullUserBody) {
            Response.success(null)
        } else if (simulateSuccess && storedUser != null) {
            Response.success(storedUser!!)
        } else {
            Response.error(
                404,
                "User not found".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun getUserPosts(userId: String): Response<UserPostsResponse> {
        return if (simulateSuccess) {
            Response.success(
                UserPostsResponse(
                    user = userId,
                    posts = storedPosts,
                    count = storedPosts.size
                )
            )
        } else {
            Response.error(
                404,
                "Posts not found".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun getHomeFeed(userId: String): Response<FeedPostsResponse> {
        return if (simulateSuccess) {
            val computedFeed = storedFeedResponse.copy(count = storedFeedResponse.posts.size)
            Response.success(computedFeed)
        } else {
            Response.error(
                500,
                "Feed error".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun getProfileFeed(userId: String): Response<FeedPostsResponse> {
        return if (simulateSuccess) {
            val postsForProfile = storedPosts.filter { it.accountId == userId }
            Response.success(FeedPostsResponse(posts = postsForProfile, count = postsForProfile.size))
        } else {
            Response.error(
                500,
                "Feed error".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun updateUserProfile(userId: String, userData: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Profile updated", user_id = userId, error = null))
        } else {
            Response.error(
                400,
                "Update failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun followUser(userId: String, targetId: String): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Followed user", user_id = userId, error = null))
        } else {
            Response.error(
                400,
                "Follow failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun unfollowUser(userId: String, targetId: String): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Unfollowed user", user_id = userId, error = null))
        } else {
            Response.error(
                400,
                "Unfollow failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun likePost(postId: String, data: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            likeCount += 1
            Response.success(ApiResponse(message = "Post liked", like_count = likeCount, error = null))
        } else {
            Response.error(
                400,
                "Like failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun unlikePost(postId: String, data: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            if (likeCount > 0) likeCount -= 1
            Response.success(ApiResponse(message = "Post unliked", like_count = likeCount, error = null))
        } else {
            Response.error(
                400,
                "Unlike failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun addComment(data: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Comment added", error = null))
        } else {
            Response.error(
                400,
                "Comment failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun searchUsers(query: String): Response<List<SearchProfileResult>> {
        return if (simulateSuccess) {
            Response.success(
                listOf(
                    SearchProfileResult(
                        id = "user1",
                        username = "testuser1",
                        firstName = "Test",
                        lastName = "User1"
                    )
                )
            )
        } else {
            Response.error(
                400,
                "Search failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    override suspend fun createPost(data: Map<String, String>): Response<ApiResponse> {
        return if (simulateSuccess) {
            Response.success(ApiResponse(message = "Post created", user_id = "post123", error = null))
        } else {
            Response.error(
                400,
                "Post creation failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }
}