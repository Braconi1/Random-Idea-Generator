package com.example.myapplication.presentation.ui.screens.firebase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.viewModel.FirebaseUiState
import com.example.myapplication.presentation.viewModel.FirebaseViewModel

@Composable
fun FirebaseScreen(
    viewModel: FirebaseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val ideas by viewModel.ideas.collectAsState()

    if (!isLoggedIn) {
        AuthSection(viewModel = viewModel, uiState = uiState)
    } else {
        IdeasSection(viewModel = viewModel, uiState = uiState, ideas = ideas)
    }
}

@Composable
fun AuthSection(
    viewModel: FirebaseViewModel,
    uiState: FirebaseUiState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isSignUp) "Create Account" else "Sign In",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is FirebaseUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (isSignUp) viewModel.signUp(email, password)
                    else viewModel.signIn(email, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSignUp) "Sign Up" else "Sign In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { isSignUp = !isSignUp }) {
                Text(if (isSignUp) "Already have an account? Sign In" else "No account? Sign Up")
            }
        }

        if (uiState is FirebaseUiState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = (uiState as FirebaseUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun IdeasSection(
    viewModel: FirebaseViewModel,
    uiState: FirebaseUiState,
    ideas: List<com.example.myapplication.model.data.repository.FirebaseIdea>
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Cloud Ideas", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { viewModel.signOut() }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && category.isNotBlank()) {
                    viewModel.saveIdea(title, description, category)
                    title = ""
                    description = ""
                    category = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save to Cloud")
        }

        if (uiState is FirebaseUiState.Error) {
            Text(
                text = (uiState as FirebaseUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ideas) { idea ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(idea.title, style = MaterialTheme.typography.titleMedium)
                            Text(idea.description, style = MaterialTheme.typography.bodySmall)
                            Text(idea.category, style = MaterialTheme.typography.labelSmall)
                        }
                        IconButton(onClick = { viewModel.deleteIdea(idea.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}