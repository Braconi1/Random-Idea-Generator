package com.example.myapplication.presentation.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.ui.screens.home.components.CategoryFilterChip
import com.example.myapplication.presentation.ui.screens.home.components.IdeaCard
import com.example.myapplication.presentation.ui.screens.home.components.SectionTitle
import com.example.myapplication.presentation.viewModel.HomeUiState
import com.example.myapplication.presentation.viewModel.HomeViewModel

// STATEFUL - prima ViewModel
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onIdeaClick: (IdeaEntity) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentIdea by viewModel.currentIdea.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    HomeContent(
        uiState = uiState,
        currentIdea = currentIdea,
        favorites = favorites,
        onGenerateClick = { viewModel.generateRandomIdea() },
        onFavoriteClick = { idea ->
            if (favorites.any { it.ideaId == idea.ideaId }) {
                viewModel.removeFromFavorites(idea)
            } else {
                viewModel.addToFavorites(idea)
            }
        },
        onIdeaClick = onIdeaClick,
        modifier = modifier
    )
}

// STATELESS - prima samo data i callbacks
@Composable
fun HomeContent(
    uiState: HomeUiState,
    currentIdea: IdeaEntity?,
    favorites: List<IdeaEntity>,
    onGenerateClick: () -> Unit,
    onFavoriteClick: (IdeaEntity) -> Unit,
    onIdeaClick: (IdeaEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val ideas = if (uiState is HomeUiState.Success) uiState.ideas else emptyList()
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember(ideas) {
        listOf("All") + ideas.map { it.categoryId.toString() }.distinct()
    }

    val filteredIdeas = remember(selectedCategory, ideas) {
        if (selectedCategory == "All") ideas
        else ideas.filter { it.categoryId.toString() == selectedCategory }
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
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Idea")
            }
        }

        item {
            when (uiState) {
                is HomeUiState.Loading -> CircularProgressIndicator()
                is HomeUiState.Error -> Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error
                )
                else -> {}
            }
        }

        item {
            if (currentIdea != null) {
                SectionTitle(text = "Your Idea")
                Spacer(modifier = Modifier.height(AppDimensions.spacingM))
                IdeaCard(
                    idea = currentIdea,
                    isFavorite = favorites.any { it.ideaId == currentIdea.ideaId },
                    onFavoriteClick = { onFavoriteClick(currentIdea) },
                    modifier = Modifier.clickable { onIdeaClick(currentIdea) }
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
                    modifier = Modifier.fillMaxWidth().padding(AppDimensions.cardPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Saved favorites:", fontWeight = FontWeight.Medium)
                    Text(text = favorites.size.toString(), fontWeight = FontWeight.Bold)
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

        item { SectionTitle(text = "All Ideas (${filteredIdeas.size})") }

        items(filteredIdeas, key = { it.ideaId }) { idea ->
            IdeaCard(
                idea = idea,
                isFavorite = favorites.any { it.ideaId == idea.ideaId },
                onFavoriteClick = { onFavoriteClick(idea) },
                modifier = Modifier.clickable { onIdeaClick(idea) }
            )
        }
    }
}