package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.model.ApiResponse
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import retrofit2.Response
class FakeLoginRepository : LoginRepository() {

    var simulateSuccess: Boolean = true
    var simulateError: Boolean = false
    var simulateLoginThrowsException = false

    var simulateSendResetCodeSuccess: Boolean = true
    var simulateSendResetCodeError: Boolean = false
    var simulateSendResetCodeThrowsException: Boolean = false
    var simulateNullUserId = false
    var simulateLoginFailureResponse = false
    var simulateErrorInBody = false



    override suspend fun login(usernameOrEmail: String, password: String): Response<ApiResponse> {
        if (simulateLoginThrowsException) {
            throw RuntimeException("Network error")
        }

        return when {
            simulateLoginFailureResponse -> {
                Response.error(500, okhttp3.ResponseBody.create(null, "Internal Server Error"))
            }
            simulateErrorInBody -> {
                Response.success(ApiResponse(message = null, error = "Simulated server-side error", user_id = null))
            }
            simulateNullUserId -> {
                Response.success(ApiResponse(message = "Login successful", error = null, user_id = null))
            }
            simulateSuccess -> {
                Response.success(ApiResponse(message = "Login successful", error = null, user_id = "fake123"))
            }
            simulateError -> {
                Response.success(ApiResponse(message = null, error = "Incorrect password", user_id = null))
            }
            else -> {
                Response.error(500, okhttp3.ResponseBody.create(null, "Something went wrong"))
            }
        }
    }





    override suspend fun sendResetCode(email: String): Response<ApiResponse> {
        if (simulateSendResetCodeThrowsException) {
            throw RuntimeException("Network error")
        }

        return if (simulateSendResetCodeSuccess) {
            Response.success(ApiResponse(message = "Reset code sent", error = null))
        } else if (simulateSendResetCodeError) {
            Response.success(ApiResponse(message = null, error = "Incorrect password"))
        } else {
            Response.error(500, okhttp3.ResponseBody.create(null, "Internal Server Error"))
        }
    }

    override suspend fun verifyResetCode(email: String, code: String): String {
        return if (code == "123456") "success" else "Invalid code"
    }

    override suspend fun resetPassword(email: String, code: String, newPassword: String): String {
        if (!simulateSuccess && simulateError) {
            return "Incorrect password" // Simulate error response
        }

        if (!simulateSuccess && !simulateError) {
            throw RuntimeException("Network error") // Simulate exception
        }

        return if (code == "123456") "success" else "Invalid code"
    }



}
