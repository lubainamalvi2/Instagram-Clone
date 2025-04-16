package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LikeButton(
    postId: String,
    currentUserId: String,
    initialIsLiked: Boolean,
    initialLikesCount: Int,
    onLikeChanged: (Boolean, Int) -> Unit
) {
    var isLiked by remember { mutableStateOf(initialIsLiked) }
    var likesCount by remember { mutableStateOf(initialLikesCount) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isLiked) "Liked" else "Not Liked",
            tint = if (isLiked) Color.Red else Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    isLiked = !isLiked
                    likesCount = if (isLiked) likesCount + 1 else likesCount - 1
                    onLikeChanged(isLiked, likesCount)
                }
        )
        Text(
            text = likesCount.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
