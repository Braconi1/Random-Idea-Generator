package com.example.myapplication.presentation.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.presentation.AppDimensions

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimensions.spacingMassive),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "",
            fontSize = AppDimensions.fontDisplay,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        Text(
            text = "Idea Generator",
            fontSize = AppDimensions.fontDisplay,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingS))

        Text(
            text = "Never be bored again.\nDiscover and save ideas every day.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth().height(AppDimensions.buttonHeight)
        ) {
            Text("Get Started", fontSize = AppDimensions.fontMedium)
        }

        Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

        OutlinedButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(AppDimensions.buttonHeight)
        ) {
            Text("Log In", fontSize = AppDimensions.fontMedium)
        }
    }
}