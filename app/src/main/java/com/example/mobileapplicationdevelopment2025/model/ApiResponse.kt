package com.example.mobileapplicationdevelopment2025.model

data class ApiResponse(
    val message: String?,
    val error: String?,
    val like_count: Int? = null,
    val user_id: String? = null,
    val code: String? = null,
)
