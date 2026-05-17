package com.example.myapplication.presentation.ui.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.ui.screens.home.components.CategoryFilterChip
import com.example.myapplication.presentation.ui.screens.home.components.IdeaCard
import com.example.myapplication.presentation.ui.screens.home.components.SectionTitle
import com.example.myapplication.presentation.viewModel.FavoritesUiState
import com.example.myapplication.presentation.viewModel.FavoritesViewModel

// STATEFUL
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    onIdeaClick: (IdeaEntity) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesContent(
        uiState = uiState,
        onRemoveFavorite = { viewModel.removeFromFavorites(it) },
        onIdeaClick = onIdeaClick,
        modifier = modifier
    )
}

// STATELESS
@Composable
fun FavoritesContent(
    uiState: FavoritesUiState,
    onRemoveFavorite: (IdeaEntity) -> Unit,
    onIdeaClick: (IdeaEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites = if (uiState is FavoritesUiState.Success) uiState.ideas else emptyList()
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember(favorites) {
        listOf("All") + favorites.map { it.categoryId.toString() }.distinct()
    }

    val filteredFavorites = remember(selectedCategory, favorites) {
        if (selectedCategory == "All") favorites
        else favorites.filter { it.categoryId.toString() == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppDimensions.contentPadding, vertical = AppDimensions.spacingM)
    ) {
        Spacer(modifier = Modifier.height(AppDimensions.spacingS))
        Text(text = "Favorites", fontSize = AppDimensions.fontXXL, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(AppDimensions.spacingS))
        Text(
            text = "Ideas you saved for later.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))

        when (uiState) {
            is FavoritesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is FavoritesUiState.Error -> {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {
                if (favorites.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "You dont have favorites yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.spacingS))
                            Text(
                                text = "Generate idea on the Home screen and save it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.spacingM)) {
                        items(categories) { category ->
                            CategoryFilterChip(
                                label = category,
                                isSelected = selectedCategory == category,
                                onClick = { selectedCategory = category }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(AppDimensions.XL))
                    SectionTitle(text = "Saved Ideas (${filteredFavorites.size})")
                    Spacer(modifier = Modifier.height(AppDimensions.spacingL))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimensions.spacingXXL)) {
                        items(filteredFavorites, key = { it.ideaId }) { idea ->
                            IdeaCard(
                                idea = idea,
                                isFavorite = true,
                                onFavoriteClick = { onRemoveFavorite(idea) },
                                modifier = Modifier.clickable { onIdeaClick(idea) }
                            )
                        }
                    }
                }
            }
        }
    }
}