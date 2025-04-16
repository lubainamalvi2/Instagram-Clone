package com.example.mobileapplicationdevelopment2025.repository

import android.util.Log
import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import retrofit2.Response

open class LoginRepository(
    private val api: com.example.mobileapplicationdevelopment2025.network.ApiService = RetrofitInstance.api
) {
    open suspend fun login(usernameOrEmail: String, password: String): Response<ApiResponse> {
        val credentials = mapOf(
            "username_or_email" to usernameOrEmail,
            "password" to password
        )
        val x = api.login(credentials)
        //Log.e("credentials", credentials.toString())
        return x
    }

    open suspend fun sendResetCode(email: String): Response<ApiResponse> {
        return api.sendResetCode(mapOf("email" to email))
    }

    open suspend fun verifyResetCode(email: String, code: String): String {
        return try {
            val response = api.verifyResetCode(mapOf("email" to email, "code" to code))
            if (response.isSuccessful) "success"
            else response.errorBody()?.string() ?: "Something went wrong"
        } catch (e: Exception) {
            e.message ?: "Network error"
        }
    }

    open suspend fun resetPassword(email: String, code: String, newPassword: String): String {
        return try {
            val response = api.resetPassword(
                mapOf("email" to email, "code" to code, "new_password" to newPassword)
            )
            if (response.isSuccessful) "success"
            else response.errorBody()?.string() ?: "Something went wrong"
        } catch (e: Exception) {
            e.message ?: "Network error"
        }
    }
}
