package com.example.myapplication.presentation.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.DetailUiState
import com.example.myapplication.presentation.viewModel.DetailViewModel

// STATEFUL
@Composable
fun IdeaDetailScreen(
    ideaId: Long,
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(ideaId) {
        viewModel.loadIdea(ideaId)
    }

    IdeaDetailContent(
        uiState = uiState,
        isFavorite = isFavorite,
        onToggleFavorite = { idea -> viewModel.toggleFavorite(idea) },
        onBack = onBack
    )
}

// STATELESS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailContent(
    uiState: DetailUiState,
    isFavorite: Boolean,
    onToggleFavorite: (IdeaEntity) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState is DetailUiState.Success)
                            uiState.idea.title else "Detail",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is DetailUiState.Success) {
                        IconButton(onClick = { onToggleFavorite(uiState.idea) }) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is DetailUiState.Success -> {
                val idea = uiState.idea
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(AppDimensions.contentPadding),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.contentPadding)
                ) {
                    Text(text = idea.title, fontSize = AppDimensions.fontTitle, fontWeight = FontWeight.Bold)

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(AppDimensions.cardPadding)) {
                            Text(text = "Description", fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(AppDimensions.spacingXS))
                            Text(text = idea.description)
                        }
                    }

                    if (isFavorite) {
                        Text(
                            text = "Saved to favorites",
                            color = Color.Green,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            else -> {}
        }
    }
}