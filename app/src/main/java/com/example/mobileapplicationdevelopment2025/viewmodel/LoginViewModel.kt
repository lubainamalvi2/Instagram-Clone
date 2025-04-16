package com.example.mobileapplicationdevelopment2025.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import com.example.mobileapplicationdevelopment2025.repository.IUserRepository
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val loginRepo: LoginRepository = LoginRepository(),
    private val userRepo: IUserRepository,
    var api: ApiService = RetrofitInstance.api
) : AndroidViewModel(application) {

    @VisibleForTesting
    var simulateUserProfileFetchFailure: Boolean = false

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            try {
                val response = loginRepo.login(usernameOrEmail, password)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.error != null) {
                        _loginResult.value = body.error
                    } else if (body?.user_id != null) {
                        _loginResult.value = "Login successful!"

                        // Fetch complete user profile
                        try {
                            val userResponse = if (simulateUserProfileFetchFailure) {
                                throw RuntimeException("Simulated profile fetch failure")
                            } else {
                                api.getUserById(body.user_id)
                            }
                            if (userResponse.isSuccessful) {
                                userResponse.body()?.let { user ->
                                    // Save complete user profile to Room
                                    userRepo.cacheUser(user)
                                    // Clear other users to ensure only the current user is stored
                                    userRepo.clearOtherUsers(user.id)
                                }
                            } else {
                                // If we can't fetch the complete profile, save minimal info
                                val user = User(
                                    id = body.user_id,
                                    username = usernameOrEmail,
                                    email = usernameOrEmail,
                                    password = password,
                                    first_name = "",
                                    last_name = ""
                                )
                                userRepo.cacheUser(user)
                                // Clear other users to ensure only the current user is stored
                                userRepo.clearOtherUsers(user.id)
                                //Log.e("LoginViewModel", "Failed to fetch user profile: ${userResponse.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            // If we can't fetch the complete profile, save minimal info
                            val user = User(
                                id = body.user_id,
                                username = usernameOrEmail,
                                email = usernameOrEmail,
                                password = password,
                                first_name = "",
                                last_name = ""
                            )
                            userRepo.cacheUser(user)
                            // Clear other users to ensure only the current user is stored
                            userRepo.clearOtherUsers(user.id)
                            Log.e("LoginViewModel", "Error fetching user profile", e)
                        }
                    }
                } else {
                    _loginResult.value = "Login failed: ${response.message()}"
                }

            } catch (e: Exception) {
                //API failed —> try Room offline login
                val cachedUser = userRepo.getCachedUser()

                if (cachedUser != null &&
                    (cachedUser.username == usernameOrEmail || cachedUser.email == usernameOrEmail) &&
                    cachedUser.password == password
                ) {
                    _loginResult.value = "Login successful (offline)"
                } else {
                    _loginResult.value = "Something went wrong. Please try again later."
                }

                //Log.e("LoginViewModel", "Network error, falling back to offline login", e)
            }
        }
    }

    fun clearResult() {
        _loginResult.value = null
    }
}
