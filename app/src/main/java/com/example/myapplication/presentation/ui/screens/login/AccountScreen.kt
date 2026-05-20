package com.example.myapplication.presentation.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.LoginViewModel

@Composable
fun AccountScreen(
    viewModel: LoginViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Account",
            modifier = Modifier.size(AppDimensions.spacingGiant),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}