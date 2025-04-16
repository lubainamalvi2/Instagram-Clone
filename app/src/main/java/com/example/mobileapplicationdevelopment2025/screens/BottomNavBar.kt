package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

@Composable
fun BottomNavBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavIcon(navController, R.drawable.feed, "feed")
            BottomNavIcon(navController, R.drawable.search, "search")
            BottomNavIcon(navController, R.drawable.add, "new_post", size = 40.dp)
            BottomNavIcon(navController, R.drawable.profile, "profile", isProfile = true)
        }
    }
}

@Composable
fun BottomNavIcon(
    navController: NavController,
    iconRes: Int,
    route: String,
    size: Dp = 32.dp,
    isProfile: Boolean = false
) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = route,
        modifier = Modifier
            .size(size)
            .clickable {
                if (isProfile) {
                    // Navigate to profile without userId to show current user
                    navController.navigate("profile") {
                        popUpTo("feed") { inclusive = false }
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate(route) {
                        popUpTo("feed") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
    )
}
