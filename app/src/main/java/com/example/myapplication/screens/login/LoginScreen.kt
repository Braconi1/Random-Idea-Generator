package com.example.myapplication.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.screens.login.components.EmailField
import com.example.myapplication.screens.login.components.LoginButton
import com.example.myapplication.screens.login.components.LoginHeader
import com.example.myapplication.screens.login.components.PasswordField

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
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader()

        Spacer(modifier = Modifier.height(27.dp))

        EmailField(
            value = email,
            onValueChange = { email = it },
            isError = showEmailError
        )

        Spacer(modifier = Modifier.height(13.dp))

        PasswordField(
            value = password,
            onValueChange = { password = it },
            isError = showPasswordError
        )

        Spacer(modifier = Modifier.height(7.dp))

        if (submitted && !canSubmit) {
            Text(
                text = "Wrong input",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LoginButton(
            enabled = canSubmit,
            onClick = {
                submitted = true
                if (canSubmit) {
                    onLoginSuccess()
                }
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

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