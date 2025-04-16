package com.example.mobileapplicationdevelopment2025.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mobileapplicationdevelopment2025.room.Converters

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey val id: String,
    val pfp: String? = null,
    val first_name: String,
    val last_name: String,
    val username: String,
    val email: String,
    val bio: String? = null,
    val links: Map<String, String>? = null,
    val password: String,
    val followers: List<String>? = null,
    val following: List<String>? = null
) {
    fun isBeingFollowedBy(userId: String): Boolean {
        return followers?.contains(userId) ?: false
    }
}
