package com.example.myapplication.presentation.uii.screens.favorites

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
import com.example.myapplication.model.Idea
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.IdeaViewModel
import com.example.myapplication.presentation.uii.screens.home.components.CategoryFilterChip
import com.example.myapplication.presentation.uii.screens.home.components.IdeaCard
import com.example.myapplication.presentation.uii.screens.home.components.SectionTitle

@Composable
fun FavoritesScreen(
    viewModel: IdeaViewModel,
    modifier: Modifier = Modifier,
    onIdeaClick: (Idea) -> Unit = {}
) {
    val favorites by viewModel.favorites.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }

    val categories by remember(favorites) {
        derivedStateOf {
            listOf("All") + favorites.map { it.category }.distinct()
        }
    }

    val filteredFavorites by remember(selectedCategory, favorites) {
        derivedStateOf {
            if (selectedCategory == "All") favorites
            else favorites.filter { it.category == selectedCategory }
        }
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
            // filter po kategoriji
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

            // treba jos doradit
            SectionTitle(text = "Saved Ideas (${filteredFavorites.size})")
            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            if (filteredFavorites.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No favorites in this category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimensions.spacingXXL)) {
                    items(filteredFavorites, key = { it.id }) { idea ->
                        IdeaCard(
                            idea = idea,
                            isFavorite = true,
                            onFavoriteClick = { viewModel.removeFromFavorites(idea) },
                            modifier = Modifier.clickable { onIdeaClick(idea) }
                        )
                        // i ovo trebam jos doradit
                    }
                }
            }
        }
    }
}