package com.example.mobileapplicationdevelopment2025.util

import android.util.Log

interface Logger {
    fun e(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)
    fun d(tag: String, message: String)
}

class AndroidLogger : Logger {
    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
} 