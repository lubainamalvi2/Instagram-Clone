package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    private val _resetResult = MutableStateFlow<String?>(null)
    val resetResult: StateFlow<String?> = _resetResult

    fun resetPassword(email: String, code: String, newPassword: String, confirmPassword: String) {
        if (newPassword.isBlank() || confirmPassword.isBlank()) {
            _resetResult.value = "Fields cannot be empty"
            return
        }

        if (newPassword != confirmPassword) {
            _resetResult.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.resetPassword(email, code, newPassword)

                _resetResult.value = if (result.contains("success", ignoreCase = true)) {
                    "success"
                } else {
                    result
                }

            } catch (e: Exception) {
                _resetResult.value = "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }


    fun clearMessage() {
        _resetResult.value = null
    }
}
