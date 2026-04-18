package com.example.myapplication.presentation.uii.screens.detail

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
import com.example.myapplication.model.hardcodedIdeas
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.IdeaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailScreen(
    ideaId: Int,
    ideaTitle: String,
    viewModel: IdeaViewModel,
    onBack: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    // trazimo ideju id-u
    val idea = hardcodedIdeas.find { it.id == ideaId }
    val isFavorite by remember(favorites) {
        derivedStateOf { favorites.any { it.id == ideaId } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ideaTitle, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (idea != null) {
                            if (isFavorite) viewModel.removeFromFavorites(idea)
                            else viewModel.addToFavorites(idea)
                        }
                    }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (idea == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Idea not found.")
            }
        } else {
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
                        Text(text = "Category", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(AppDimensions.spacingXS))
                        Text(text = idea.category, color = Color.Blue)
                    }
                }

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
    }
}