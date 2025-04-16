package com.example.mobileapplicationdevelopment2025.repository

import android.content.Context
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import com.example.mobileapplicationdevelopment2025.room.UserDao
import com.example.mobileapplicationdevelopment2025.room.UserDatabase

class UserRepository(
    private val userDao: UserDao,
    private val api: ApiService
) : IUserRepository {

    constructor(context: Context) : this(
        UserDatabase.getDatabase(context).userDao(),
        RetrofitInstance.api
    )

    override suspend fun registerUserRemote(user: User) = api.registerUser(user)

    override suspend fun cacheUser(user: User) = userDao.insertUser(user)

    override suspend fun getCachedUser() = userDao.getCurrentUser()

    override suspend fun clearOtherUsers(currentUserId: String) =
        userDao.clearOtherUsers(currentUserId)
}
