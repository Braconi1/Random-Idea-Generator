package com.example.myapplication.presentation.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.presentation.AppDimensions

@Composable
fun IdeaCard(
    idea: IdeaEntity,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(AppDimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = idea.title, fontSize = AppDimensions.fontLarge)
                Spacer(modifier = Modifier.height(AppDimensions.spacingXS))
                Text(text = idea.description, color = Color.Gray)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove" else "Add",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}