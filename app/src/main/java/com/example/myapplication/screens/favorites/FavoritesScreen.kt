package com.example.myapplication.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.screens.IdeaViewModel
import com.example.myapplication.screens.home.components.IdeaCard
import com.example.myapplication.screens.home.components.SectionTitle
import com.example.myapplication.screens.Idea

@Composable
fun FavoritesScreen(
    viewModel: IdeaViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "Favorites",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Ideas you saved for later.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "You dont have favorites yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Generate idea on the Home screen and save it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // treba jos doradit
            SectionTitle(text = "Saved Ideas (${favorites.size})")
            Spacer(modifier = Modifier.height(11.dp))

            LazyColumn {
                items(favorites) { idea ->
                    Column {
                        IdeaCard(
                            idea = idea,
                            isFavorite = true,
                            onFavoriteClick = { viewModel.removeFromFavorites(idea) }
                        )
                        // i ovo trebam jos doradit
                        Spacer(modifier = Modifier.height(13.dp))
                    }
                }
            }
        }
    }
}