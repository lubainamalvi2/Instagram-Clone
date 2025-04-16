package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    private val _sendCodeResult = MutableStateFlow<String?>(null)
    val sendCodeResult: StateFlow<String?> = _sendCodeResult

    fun sendResetCode(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.sendResetCode(email)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.error != null) {
                        _sendCodeResult.value = body.error
                    } else {
                        val code = body?.code ?: "N/A"
                        // uncomment below if email is taking too long to send
                        //android.util.Log.d("ForgotPasswordViewModel", "Reset code: $code") // to help TAs grade quicker and not have to wait for an email
                        _sendCodeResult.value = "success"
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    _sendCodeResult.value = error
                }
            } catch (e: Exception) {
                _sendCodeResult.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearResult() {
        _sendCodeResult.value = null
    }
}

