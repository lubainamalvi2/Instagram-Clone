package com.example.mobileapplicationdevelopment2025.screens


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.viewmodel.ResetPasswordViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.testTag



@Composable
fun ResetPwdPage(
    navController: NavController,
    email: String,
    code: String,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val resetResult by viewModel.resetResult.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(resetResult) {
        resetResult?.let {
            when {
                it == "success" -> {
                    Toast.makeText(context, "Password reset successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                it.contains("Fields cannot be empty", ignoreCase = true) -> {
                    Toast.makeText(context, "Both password fields are required", Toast.LENGTH_SHORT).show()
                }
                it.contains("Passwords do not match", ignoreCase = true) -> {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show()
                    Log.e("ResetPwdPage", "Unhandled error: $it")
                }
            }

            viewModel.clearMessage()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF833AB4), Color(0xFFF77737)))
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Reset Password", fontSize = 24.sp, color = Color.White)

            Spacer(Modifier.height(24.dp))

            BasicTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("new_password_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (newPassword.isEmpty()) {
                            Text(
                                text = "New Password",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )


            Spacer(Modifier.height(12.dp))

            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("confirm_password_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (confirmPassword.isEmpty()) {
                            Text(
                                text = "Confirm Password",
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
                    viewModel.resetPassword(email, code, newPassword, confirmPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("submit_reset_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff612ebf))
            ) {
                Text("Reset Password", color = Color.White)
            }

            Spacer(Modifier.height(8.dp))

            resetResult?.let {
                if (it != "success") {
                    Text(it, color = Color.Red)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") },
                modifier = Modifier.testTag("cancel_button")
            ) {
                Text("Cancel and go back to login", color = Color.White)
            }
        }
    }
}
