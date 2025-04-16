package com.example.mobileapplicationdevelopment2025.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.Validator
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.repository.IUserRepository
import com.example.mobileapplicationdevelopment2025.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    application: Application,
    private val repository: IUserRepository
) : AndroidViewModel(application) {


    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    fun register(user: User, confirmPassword: String) {
        viewModelScope.launch {

            if (user.first_name.isBlank() || user.last_name.isBlank() ||
                user.email.isBlank() || user.username.isBlank() || user.password.isBlank()) {
                _registerResult.value = "All fields are required."
                //Log.e("RegisterViewModel", "Validation failed: missing required fields")
                return@launch
            }

            if (user.password != confirmPassword) {
                _registerResult.value = "Passwords do not match."
                //Log.e("RegisterViewModel", "Validation failed: passwords do not match")
                return@launch
            }

            if (!Validator.isEmailValid(user.email)) {
                _registerResult.value = "Invalid email format."
                return@launch
            }

            try {
                val response = repository.registerUserRemote(user)

                if (response.isSuccessful && response.body()?.user_id != null) {
                    val savedUser = user.copy(id = response.body()!!.user_id!!)
                    repository.cacheUser(savedUser)
                    repository.clearOtherUsers(savedUser.id)
                    _registerResult.value = "Registration successful"
                    //Log.d("RegisterViewModel", "User registered: ${savedUser.username}")
                } else {
                    val error = response.errorBody()?.string() ?: "Something went wrong"
                    //Log.e("RegisterViewModel", "API error: $error")

                    _registerResult.value = if (error.contains("Username or email already exists", ignoreCase = true)) {
                        "Username or email already exists"
                    } else {
                        "Something went wrong"
                    }

                }

            } catch (e: Exception) {
                _registerResult.value = "Something went wrong"
                //Log.e("RegisterViewModel", "Exception during registration", e)
            }
        }
    }
    fun clearResult() {
        _registerResult.value = null
    }


}
