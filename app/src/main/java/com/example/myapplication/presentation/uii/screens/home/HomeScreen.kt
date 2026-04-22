package com.example.myapplication.presentation.uii.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.model.Idea
import com.example.myapplication.model.hardcodedIdeas
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.uii.screens.home.components.CategoryFilterChip
import com.example.myapplication.presentation.uii.screens.home.components.IdeaCard
import com.example.myapplication.presentation.uii.screens.home.components.SectionTitle
import com.example.myapplication.presentation.viewModel.IdeaViewModel

@Composable
fun HomeScreen(
    viewModel: IdeaViewModel,
    modifier: Modifier = Modifier,
    onIdeaClick: (Idea) -> Unit = {}
) {
    val currentIdea by viewModel.currentIdea.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val categories = remember {
        listOf("All") + hardcodedIdeas.map { it.category }.distinct()
    }

    var selectedCategory by remember { mutableStateOf("All") }

    val filteredIdeas by remember(selectedCategory) {
        derivedStateOf {
            if (selectedCategory == "All") hardcodedIdeas
            else hardcodedIdeas.filter { it.category == selectedCategory }
        }
    }

    val favoritesCount by remember {
        derivedStateOf { favorites.size }
    }

    val hasCurrentIdea by remember {
        derivedStateOf { currentIdea != null }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppDimensions.contentPadding),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.XL)
    ) {
        item {
            Text(text = "Bored: Get an Idea", fontSize = AppDimensions.fontXL, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(AppDimensions.spacingXS))
            Text(text = "Touch button to generate your idea :)")
            Spacer(modifier = Modifier.height(AppDimensions.spacingXXXL))
            Button(
                onClick = { viewModel.generateRandomIdea() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Idea")
            }
        }

        item {
            if (hasCurrentIdea) {
                SectionTitle(text = "Your Idea")
                Spacer(modifier = Modifier.height(AppDimensions.spacingM))
                IdeaCard(
                    idea = currentIdea!!,
                    isFavorite = favorites.any { it.id == currentIdea!!.id },
                    onFavoriteClick = {
                        if (favorites.any { it.id == currentIdea!!.id }) {
                            viewModel.removeFromFavorites(currentIdea!!)
                        } else {
                            viewModel.addToFavorites(currentIdea!!)
                        }
                    },
                    modifier = Modifier.clickable { onIdeaClick(currentIdea!!) }
                )
            } else {
                Text(
                    text = "Tap Generate Idea to get started",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.cardPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Saved favorites:", fontWeight = FontWeight.Medium)
                    Text(text = favoritesCount.toString(), fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            SectionTitle(text = "Browse by Category")
            Spacer(modifier = Modifier.height(AppDimensions.spacingM))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.spacingM)) {
                items(categories) { category ->
                    CategoryFilterChip(
                        label = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category }
                    )
                }
            }
        }

        item {
            SectionTitle(text = "All Ideas (${filteredIdeas.size})")
        }

        if (filteredIdeas.isEmpty()) {
            item {
                Text(
                    text = "No ideas available in this category.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filteredIdeas, key = { it.id }) { idea ->
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
                    modifier = Modifier.clickable { onIdeaClick(idea) }
                )
            }
        }
    }
}