package com.example.mobileapplicationdevelopment2025

object Validator {
    fun isEmailValid(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
