package com.example.mobileapplicationdevelopment2025.fakes

import android.app.Application
import android.content.Context
import org.mockito.Mockito

class FakeApplication : Application() {
    override fun getApplicationContext(): Context {
        return this
    }
}
