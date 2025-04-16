package com.example.mobileapplicationdevelopment2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationdevelopment2025.navigation.AppNavGraph
import com.example.mobileapplicationdevelopment2025.ui.theme.MobileApplicationDevelopment2025Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileApplicationDevelopment2025Theme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavGraph(navController = navController)
    }
}
