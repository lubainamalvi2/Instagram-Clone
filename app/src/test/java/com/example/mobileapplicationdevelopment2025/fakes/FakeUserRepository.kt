package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.repository.IUserRepository
import okhttp3.ResponseBody
import retrofit2.Response

class FakeUserRepository : IUserRepository {

    var simulateSuccess = true
    var simulateDuplicate = false
    private var cachedUser: User? = null

    override suspend fun registerUserRemote(user: User): Response<ApiResponse> {
        return when {
            simulateDuplicate -> Response.success(ApiResponse(null, "Username or email already exists", null, null))
            simulateSuccess -> Response.success(ApiResponse("Registered", null, null, "user123"))
            else -> Response.error(500, ResponseBody.create(null, "Something went wrong"))
        }
    }

    var lastCachedUser: User? = null

    override suspend fun cacheUser(user: User) {
        lastCachedUser = user
        cachedUser = user
    }


    override suspend fun getCachedUser(): User? {
        return cachedUser
    }

    override suspend fun clearOtherUsers(currentUserId: String) {
        cachedUser = null
    }


}
