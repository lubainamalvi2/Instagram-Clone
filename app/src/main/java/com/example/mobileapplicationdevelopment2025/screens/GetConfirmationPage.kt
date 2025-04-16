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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.viewmodel.ForgotPasswordViewModel
import androidx.compose.ui.platform.testTag


@Composable
fun GetConfirmationPage(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val sendCodeResult by viewModel.sendCodeResult.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF833AB4), Color(0xFFF77737))))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Reset your password", fontSize = 24.sp, color = Color.White)

            Spacer(Modifier.height(24.dp))

            BasicTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("email_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (email.isEmpty()) {
                            Text(
                                text = "Email",
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
                    viewModel.sendResetCode(email)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_code_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff612ebf))
            ) {
                Text("Send me a code", color = Color.White)
            }

            Spacer(Modifier.height(8.dp))

            val context = LocalContext.current
            val currentResult = sendCodeResult

            LaunchedEffect(currentResult) {
                currentResult?.let {
                    when {
                        it.contains("success", ignoreCase = true) -> {
                            Toast.makeText(context, "Code sent to your email", Toast.LENGTH_SHORT).show()
                            kotlinx.coroutines.delay(1000)
                            viewModel.clearResult()
                            navController.navigate("enter_confirmation?email=$email")
                        }

                        it.contains("No account", ignoreCase = true) -> {
                            Toast.makeText(context, "No account found with that email", Toast.LENGTH_SHORT).show()
                        }

                        it.contains("invalid", ignoreCase = true) -> {
                            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        }

                        it.contains("send", ignoreCase = true) -> {
                            Toast.makeText(context, "Unable to send email", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show()
                            Log.e("GetConfirmationPage", "Unhandled error: $it")
                        }
                    }

                    viewModel.clearResult()
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White)
                Text("  or  ", color = Color.White)
                Divider(modifier = Modifier.weight(1f), color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("new_account") },
                modifier = Modifier.testTag("create_account_button")
            ) {
                Text("Create a new account", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() },
                modifier = Modifier.testTag("back_to_login_button")
            ) {
                Text("Back to login", color = Color.White)
            }
        }
    }
}
