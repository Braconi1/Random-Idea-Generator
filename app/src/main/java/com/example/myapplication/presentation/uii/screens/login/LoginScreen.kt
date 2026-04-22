package com.example.myapplication.presentation.uii.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.uii.screens.login.components.EmailField
import com.example.myapplication.presentation.uii.screens.login.components.LoginButton
import com.example.myapplication.presentation.uii.screens.login.components.LoginHeader
import com.example.myapplication.presentation.uii.screens.login.components.PasswordField

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    var showEmailError = false
    if (email.isNotEmpty()) {
        if (!email.contains("@") || !email.contains(".")) {
            showEmailError = true
        }
    }

    var showPasswordError = false
    if (password.isNotEmpty()) {
        if (password.length < 6) {
            showPasswordError = true
        }
    }

    var canSubmit = false
    if (email.contains("@") && email.contains(".")) {
        if (password.length >= 6) {
            canSubmit = true
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(AppDimensions.spacingMassive),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader()

        Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))

        EmailField(value = email, onValueChange = { email = it }, isError = showEmailError)

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        PasswordField(value = password, onValueChange = { password = it }, isError = showPasswordError)

        Spacer(modifier = Modifier.height(AppDimensions.spacingS))

        if (submitted && !canSubmit) {
            Text(
                text = "Wrong input",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        LoginButton(
            enabled = canSubmit,
            onClick = {
                submitted = true
                if (canSubmit) onLoginSuccess()
            }
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = {}) {
                Text(text = "Sign Up")
            }
        }
    }
}