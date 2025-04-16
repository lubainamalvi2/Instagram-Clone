package com.example.mobileapplicationdevelopment2025.model

import com.google.gson.annotations.SerializedName

data class UserPostsResponse(
    val user: String,
    val posts: List<Post>,
    val count: Int
)

data class Post(
    val id: String,
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val caption: String,
    val timestamp: String,
    val likes: Int,
    val author: Author?,
    @SerializedName("comment_details")
    val commentDetails: List<Comment>?,
    @SerializedName("is_liked")
    val isLiked: Boolean = false
)

data class Comment(
    val id: String,
    val text: String,
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("post_id")
    val postId: String,
    val timestamp: String,
    val username: String? = null

)
data class Author(
    val id: String,
    val username: String,
    val pfp: String?
)
