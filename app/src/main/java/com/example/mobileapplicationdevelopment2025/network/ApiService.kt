package com.example.mobileapplicationdevelopment2025.network

import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.model.FeedPostsResponse
import com.example.mobileapplicationdevelopment2025.model.SearchProfileResult
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.model.UserPostsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // For login and register
    @POST("/api/register")
    suspend fun registerUser(@Body user: User): Response<ApiResponse>

    @POST("/api/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<ApiResponse>

    @POST("/api/send-reset-code")
    suspend fun sendResetCode(@Body email: Map<String, String>): Response<ApiResponse>

    @POST("/api/verify-reset-code")
    suspend fun verifyResetCode(@Body body: Map<String, String>): Response<ApiResponse>

    @POST("/api/reset-password")
    suspend fun resetPassword(@Body data: Map<String, String>): Response<ApiResponse>

    @GET("/api/user/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): Response<User>

    @GET("/api/user/{userId}/posts")
    suspend fun getUserPosts(@Path("userId") userId: String): Response<UserPostsResponse>

    @GET("/api/feed/home/{user_id}")
    suspend fun getHomeFeed(@Path("user_id") userId: String): Response<FeedPostsResponse>

    @GET("/api/feed/profile/{user_id}")
    suspend fun getProfileFeed(@Path("user_id") userId: String): Response<FeedPostsResponse>

    @PUT("/api/user/update/{userId}")
    suspend fun updateUserProfile(
        @Path("userId") userId: String,
        @Body userData: Map<String, String>
    ): Response<ApiResponse>
    
    @PUT("/api/user/{userId}/follow/{targetId}")
    suspend fun followUser(
        @Path("userId") userId: String,
        @Path("targetId") targetId: String
    ): Response<ApiResponse>

    @PUT("/api/user/{userId}/unfollow/{targetId}")
    suspend fun unfollowUser(
        @Path("userId") userId: String,
        @Path("targetId") targetId: String
    ): Response<ApiResponse>
    @POST("/api/posts/{post_id}/like")
    suspend fun likePost(@Path("post_id") postId: String, @Body data: Map<String, String>): Response<ApiResponse>

    @POST("/api/posts/{post_id}/unlike")
    suspend fun unlikePost(@Path("post_id") postId: String, @Body data: Map<String, String>): Response<ApiResponse>

    @POST("/api/comments")
    suspend fun addComment(@Body data: Map<String, String>): Response<ApiResponse>

    @GET("/api/search")
    suspend fun searchUsers(@Query("query") query: String): Response<List<SearchProfileResult>>

    @POST("/api/posts")
    suspend fun createPost(@Body data: Map<String, String>): Response<ApiResponse>
}
