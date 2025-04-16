package com.example.mobileapplicationdevelopment2025.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapplicationdevelopment2025.R
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.example.mobileapplicationdevelopment2025.viewmodel.RegisterViewModelFactory
import androidx.compose.ui.platform.testTag


@Composable
fun NewAccountPage(navController: NavController) {
    val context = LocalContext.current
    val app = context.applicationContext as android.app.Application
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(app))

    val result by viewModel.registerResult.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF833AB4), Color(0xFFF77737))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.instagram_cursive),
                contentDescription = "Instagram Logo",
                modifier = Modifier.height(80.dp)
            )

            Spacer(Modifier.height(8.dp))
            Text("Register", fontSize = 24.sp, color = Color.White)

            Spacer(Modifier.height(24.dp))

            InputWithLabel("First Name", firstName, testTag = "first_name_input") { firstName = it }
            InputWithLabel("Last Name", lastName, testTag = "last_name_input") { lastName = it }
            InputWithLabel("Email", email, keyboardType = KeyboardType.Email, testTag = "email_input") { email = it }
            InputWithLabel("Username", username, testTag = "username_input") { username = it }
            InputWithLabel("Password", password, isPassword = true, testTag = "password_input") { password = it }
            InputWithLabel(
                "Confirm Password",
                confirmPassword,
                isPassword = true,
                testTag = "confirm_password_input"
            ) { confirmPassword = it }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val user = User(
                        id = "",
                        first_name = firstName,
                        last_name = lastName,
                        email = email,
                        username = username,
                        password = password
                    )
                    viewModel.clearResult()
                    viewModel.register(user, confirmPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_account_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff612ebf),
                )
            ) {
                Text(
                    "Create Account",
                    color = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White)
                Text("  or  ", color = Color.White)
                Divider(modifier = Modifier.weight(1f), color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") },
                modifier = Modifier.testTag("already_have_account_button")
            ) {
                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Login")
                        }
                    },
                    color = Color.White
                )
            }

            val context = LocalContext.current
            val currentResult = result

            LaunchedEffect(currentResult) {
                currentResult?.let {
                    when {
                        it.contains("success", ignoreCase = true) -> {
                            Toast.makeText(
                                context,
                                "Account created successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(1000)
                            navController.navigate("feed")
                        }

                        it.contains("Passwords do not match", ignoreCase = true) -> {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT)
                                .show()
                        }

                        it.contains("Invalid email format", ignoreCase = true) -> {
                            Toast.makeText(
                                context,
                                "Please enter a valid email address",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        it.contains("Username or email already exists", ignoreCase = true) -> {
                            Toast.makeText(
                                context,
                                "Username or email is already taken",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        it.contains("required", ignoreCase = true) -> {
                            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            Toast.makeText(
                                context,
                                "Something went wrong. Please try again later.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("NewAccountPage", "Unhandled error: $it")
                        }
                    }

                    viewModel.clearResult()
                }
            }
        }
    }
}
@Composable
fun InputWithLabel(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    labelColor: Color = Color.White,
    testTag: String = "",
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField: @Composable () -> Unit ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(label, color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}


