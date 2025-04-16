package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.util.Logger

class FakeLogger : Logger {
    var lastErrorTag: String? = null
    var lastErrorMessage: String? = null
    var lastErrorThrowable: Throwable? = null
    var lastDebugTag: String? = null
    var lastDebugMessage: String? = null

    override fun e(tag: String, message: String) {
        lastErrorTag = tag
        lastErrorMessage = message
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        lastErrorTag = tag
        lastErrorMessage = message
        lastErrorThrowable = throwable
    }

    override fun d(tag: String, message: String) {
        lastDebugTag = tag
        lastDebugMessage = message
    }
} 