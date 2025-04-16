package com.example.mobileapplicationdevelopment2025.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.R
import com.example.mobileapplicationdevelopment2025.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import com.example.mobileapplicationdevelopment2025.viewmodel.LoginViewModelFactory

@Composable
fun LoginPage(navController: NavController) {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(app)
    )

    val loginResult by viewModel.loginResult.collectAsState()
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF833AB4), Color(0xFFF77737))
                )
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.instagram_cursive),
                contentDescription = "Instagram Logo",
                modifier = Modifier.height(100.dp)
            )

            Spacer(Modifier.height(8.dp))
            Text("Login", fontSize = 24.sp, color = Color.White)

            Spacer(Modifier.height(24.dp))

            BasicTextField(
                value = usernameOrEmail,
                onValueChange = { usernameOrEmail = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("username_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (usernameOrEmail.isEmpty()) {
                            Text(
                                text = "Username or Email",
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
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .testTag("password_input"),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (password.isEmpty()) {
                            Text(
                                text = "Password",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    navController.navigate("get_confirmation")
                }) {
                    Text("Forgot password?", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(usernameOrEmail, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff612ebf))
            ) {
                Text("Log In", color = Color.White)
            }


            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White)
                Text("  or  ", color = Color.White)
                Divider(modifier = Modifier.weight(1f), color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("new_account") }) {
                Text(buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Sign up.")
                    }
                }, color = Color.White, textAlign = TextAlign.Center)
            }

            loginResult?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = Color.White)
                if (it.contains("success", ignoreCase = true)) {
                    LaunchedEffect(Unit) {
                        delay(1000)
                        navController.navigate("feed")
                    }
                }
            }
        }

        val context = LocalContext.current

        LaunchedEffect(loginResult) {
            loginResult?.let {
                if (it.contains("success", ignoreCase = true)) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    delay(1000)
                    navController.navigate("feed")
                } else if (it.contains("UNAUTHORIZED", ignoreCase = true) || it.contains("not found", ignoreCase = true)) {
                    Toast.makeText(context, "Incorrect username/email or password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginPage", "Unhandled login error: $it")
                }

                viewModel.clearResult()
            }
        }

    }
}
