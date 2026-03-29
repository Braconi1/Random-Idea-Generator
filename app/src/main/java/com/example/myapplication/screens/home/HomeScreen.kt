package com.example.myapplication.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.screens.home.components.IdeaCard
import com.example.myapplication.screens.IdeaViewModel

@Composable
fun HomeScreen(
    viewModel: IdeaViewModel,
    modifier: Modifier = Modifier
) {
    val currentIdea by viewModel.currentIdea.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "Bored? Get an Idea", fontSize = 23.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Touch button to generate your idea :)")

        Spacer(modifier = Modifier.height(21.dp))

        Button(
            onClick = {
                viewModel.generateRandomIdea()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Idea")
        }

        Spacer(modifier = Modifier.height(20.dp))

        val idea = currentIdea
        if (idea != null) {
            Text(text = "Your Idea")
            Spacer(modifier = Modifier.height(12.dp))

            IdeaCard(
                idea = idea,
                isFavorite = favorites.any { it.id == idea.id },
                onFavoriteClick = {
                    if (favorites.any { it.id == idea.id }) {
                        viewModel.removeFromFavorites(idea)
                    } else {
                        viewModel.addToFavorites(idea)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(text = "Tap Generate Idea to get started")
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(text = "Favorites count")

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Saved:")
            Text(text = favorites.size.toString())
        }
    }
}