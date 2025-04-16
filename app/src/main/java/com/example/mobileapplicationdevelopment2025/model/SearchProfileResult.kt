package com.example.mobileapplicationdevelopment2025.model

import com.google.gson.annotations.SerializedName

data class SearchProfileResult(
    val id: String,
    val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val pfp: String? = null
)