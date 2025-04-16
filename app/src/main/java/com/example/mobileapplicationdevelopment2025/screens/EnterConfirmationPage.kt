package com.example.mobileapplicationdevelopment2025.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.testTag


@Composable
fun EnterConfirmationPage(
    navController: NavController,
    email: String
) {
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val repo = remember { LoginRepository() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF833AB4), Color(0xFFF77737))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enter Confirmation Code", fontSize = 24.sp, color = Color.White)

            Spacer(Modifier.height(24.dp))


            BasicTextField(
                value = code,
                onValueChange = { code = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("confirmation_code_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (code.isEmpty()) {
                            Text(
                                text = "Confirmation Code",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )


            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = repo.verifyResetCode(email, code)
                        message = if (result.contains("success", ignoreCase = true)) {
                            navController.navigate("reset_password?email=$email&code=$code")
                            null // clear message after nav
                        } else {
                            result
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("go_to_reset_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff612ebf))
            ) {
                Text("Take me to reset my password", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = {
                coroutineScope.launch {
                    val resendResponse = repo.sendResetCode(email)
                    if (resendResponse.isSuccessful && resendResponse.body()?.error == null) {
                        message = "Code resent!"
                    } else {
                        message = resendResponse.body()?.error ?: "Something went wrong"
                    }
                }
            },
                modifier = Modifier.testTag("resend_code_button")
            ) {
                Text("Didn't receive a code? Resend code.", color = Color.White)
            }

            val context = LocalContext.current

            LaunchedEffect(message) {
                message?.let {
                    when {
                        it.contains("success", ignoreCase = true) -> {
                            Toast.makeText(context, "Code resent!", Toast.LENGTH_SHORT).show()
                        }
                        it.contains("invalid", ignoreCase = true) || it.contains("expired", ignoreCase = true) -> {
                            Toast.makeText(context, "Invalid or expired code", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show()
                            Log.e("EnterConfirmationPage", "Unhandled code message: $it")
                        }
                    }
                }
            }


            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White)
                Text("  or  ", color = Color.White)
                Divider(modifier = Modifier.weight(1f), color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate("get_confirmation") },
                modifier = Modifier.testTag("back_to_email_button")
            ) {
                Text("Back to enter email", color = Color.White)
            }
        }
    }
}
