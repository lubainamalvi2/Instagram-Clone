package com.example.mobileapplicationdevelopment2025.repository

import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import retrofit2.Response
// created so that we can inject FakeUserRepository (that implements IUserRepository
// into ViewModelTests
interface IUserRepository {
    suspend fun registerUserRemote(user: User): Response<ApiResponse>
    suspend fun cacheUser(user: User)
    suspend fun getCachedUser(): User?
    suspend fun clearOtherUsers(currentUserId: String)
}
