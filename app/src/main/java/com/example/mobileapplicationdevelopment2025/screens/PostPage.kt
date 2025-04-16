package com.example.mobileapplicationdevelopment2025.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mobileapplicationdevelopment2025.R
import com.example.mobileapplicationdevelopment2025.model.Author
import com.example.mobileapplicationdevelopment2025.model.Post
import com.example.mobileapplicationdevelopment2025.model.Comment
import com.example.mobileapplicationdevelopment2025.viewmodel.LikeButton
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.TimeZone
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostPage(
    post: Post,
    navController: NavController,
    currentUserId: String,
    initialIsLiked: Boolean = false,
    onLikeChanged: (Boolean, Int) -> Unit = { _, _ -> },
    onCommentAdded: (postId: String, commentText: String) -> Unit = { _, _ -> },
    currentUsername: String
) {
    val author: Author = post.author ?: Author(id = "", username = "Unknown", pfp = null)
    var showComments by remember { mutableStateOf(false) }
    val commentList = remember { mutableStateListOf<Comment>().apply {
        addAll(post.commentDetails ?: emptyList())
    } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        println("Navigating with author.id = ${author.id}")
                        navController.navigate("profile?userId=${author.id}")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!author.pfp.isNullOrEmpty()) {
                    GlideImage(
                        model = author.pfp,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = author.username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GlideImage(
                model = post.imageUrl,
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LikeButton(
                    postId = post.id,
                    currentUserId = currentUserId,
                    initialIsLiked = post.isLiked,
                    initialLikesCount = post.likes,
                    onLikeChanged = onLikeChanged
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.ChatBubbleOutline,
                    contentDescription = "Comment",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { showComments = !showComments }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = commentList.size.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "${author.username} ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = post.caption,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (showComments) {
                CommentsSection(
                    comments = commentList,
                    onCommentSubmitted = { newComment ->
                        val tempComment = Comment(
                            id = "temp_${System.currentTimeMillis()}",
                            text = newComment,
                            accountId = currentUserId,
                            postId = post.id,
                            timestamp = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.getDefault())
                                .apply { timeZone = TimeZone.getTimeZone("GMT") }
                                .format(Date()),
                            username = currentUsername
                        )
                        commentList.add(tempComment)
                        onCommentAdded(post.id, newComment)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = formatTimestamp(post.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

fun formatTimestamp(timestamp: String): String {
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT")

    return try {
        val postDate: Date = inputFormat.parse(timestamp) ?: return timestamp
        val now = Date()
        val diffMillis = now.time - postDate.time

        val diffMinutes = diffMillis / (60 * 1000)
        val diffHours = diffMillis / (60 * 60 * 1000)

        when {
            diffMinutes < 60 -> "$diffMinutes minutes ago"
            diffHours < 24 -> "$diffHours hours ago"
            else -> {
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                outputFormat.format(postDate)
            }
        }
    } catch (e: Exception) {
        println("Date formatting error: ${e.message}")
        timestamp
    }
}

@Composable
fun CommentsSection(
    comments: List<Comment>,
    onCommentSubmitted: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .verticalScroll(scrollState)
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            comments.forEach { comment ->
                val displayName = comment.username ?: comment.accountId
                Text(
                    text = "$displayName: ${comment.text}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Add a comment") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            textStyle = TextStyle(fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Post",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                if (commentText.trim().isNotEmpty()) {
                    onCommentSubmitted(commentText.trim())
                    commentText = ""
                }
            }
        )
    }
}
