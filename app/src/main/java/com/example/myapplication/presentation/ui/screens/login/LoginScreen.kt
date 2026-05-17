package com.example.myapplication.presentation.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.ui.screens.login.components.EmailField
import com.example.myapplication.presentation.ui.screens.login.components.LoginButton
import com.example.myapplication.presentation.ui.screens.login.components.LoginHeader
import com.example.myapplication.presentation.ui.screens.login.components.PasswordField
import com.example.myapplication.presentation.viewModel.LoginUiState
import com.example.myapplication.presentation.viewModel.LoginViewModel

// statefull funkcija
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    var showWelcomeBack by remember { mutableStateOf(false) }
    var welcomeName by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.WelcomeBack -> {
                welcomeName = state.username
                showWelcomeBack = true
            }
            is LoginUiState.Success -> {
                onLoginSuccess()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    LoginContent(
        email = email,
        password = password,
        uiState = uiState,
        showWelcomeBack = showWelcomeBack,
        welcomeName = welcomeName,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
        onContinue = {
            showWelcomeBack = false
            onLoginSuccess()
            viewModel.resetState()
        },
        onSignUp = onSignUp,
        modifier = modifier
    )
}

// stateless funkcije

//assigment 4 i 3
@Composable
fun LoginContent(
    email: String,
    password: String,
    uiState: LoginUiState,
    showWelcomeBack: Boolean,
    welcomeName: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onContinue: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showEmailError = email.isNotEmpty() && (!email.contains("@") || !email.contains("."))
    val showPasswordError = password.isNotEmpty() && password.length < 6
    val canSubmit = email.contains("@") && email.contains(".") && password.length >= 6

    if (showWelcomeBack) {
        Column(
            modifier = modifier.fillMaxSize().padding(AppDimensions.spacingMassive),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = welcomeName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().height(AppDimensions.buttonHeight)
            ) {
                Text("Continue")
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().padding(AppDimensions.spacingMassive),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()
            Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))
            EmailField(value = email, onValueChange = onEmailChange, isError = showEmailError)
            Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))
            PasswordField(value = password, onValueChange = onPasswordChange, isError = showPasswordError)
            Spacer(modifier = Modifier.height(AppDimensions.spacingS))

            if (uiState is LoginUiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))
            LoginButton(enabled = canSubmit, onClick = onLogin)
            Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onSignUp) {
                    Text(text = "Sign Up")
                }
            }
        }
    }
}