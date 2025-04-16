package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.room.UserDao

class FakeUserDao : UserDao {
    private var storedUser: User? = null

    override suspend fun insertUser(user: User) {
        storedUser = transformUserLinks(user)
    }

    override suspend fun updateUser(user: User) {
        storedUser = transformUserLinks(user)
    }

    override suspend fun getCurrentUser(): User? {
        return storedUser
    }

    override suspend fun clearOtherUsers(currentUserId: String) {
        if (storedUser?.id != currentUserId) {
            storedUser = null
        }
    }

    private fun transformUserLinks(user: User): User {
        val transformedLinks = user.links?.mapValues { (_, url) ->
            when {
                url.startsWith("http://") -> url.replace("http://", "https://")
                url.startsWith("https://") -> url
                url.startsWith("www.") -> "https://$url"
                else -> "https://www.$url"
            }
        }
        return user.copy(links = transformedLinks)
    }
}
