package com.example.myapplication.presentation.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.LoginUiState
import com.example.myapplication.presentation.viewModel.LoginViewModel

@Composable
fun SignUpScreen(
    viewModel: LoginViewModel,
    onSignUpSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    val canSubmit = username.isNotBlank()
            && email.contains("@") && email.contains(".")
            && password.length >= 6
            && password == confirmPassword

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onSignUpSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(AppDimensions.spacingMassive),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            fontSize = AppDimensions.fontXXL,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingS))

        Text(
            text = "Sign up to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            placeholder = { Text("e.g. john_doe") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("e.g. john@example.com") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Min 6 characters") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            placeholder = { Text("Repeat your password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingS))

        if (uiState is LoginUiState.Error) {
            Text(
                text = (uiState as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (uiState is LoginUiState.Loading) {
            Spacer(modifier = Modifier.height(AppDimensions.spacingS))
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        Button(
            onClick = {
                submitted = true
                if (canSubmit) viewModel.register(username, email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.buttonHeight)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(AppDimensions.spacingL))

        TextButton(onClick = onBackToLogin) {
            Text("Already have an account? Log in")
        }
    }
}