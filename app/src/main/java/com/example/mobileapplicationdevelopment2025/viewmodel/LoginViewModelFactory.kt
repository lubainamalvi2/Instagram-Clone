package com.example.mobileapplicationdevelopment2025.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import com.example.mobileapplicationdevelopment2025.repository.UserRepository

class LoginViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                application,
                loginRepo = LoginRepository(),
                userRepo = UserRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
