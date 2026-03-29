package com.example.myapplication.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.screens.Idea
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun IdeaCard(
    idea: Idea,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = idea.title,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = idea.description,
                    color = Color.Gray
                )

                if (idea.category != "") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = idea.category,
                        color = Color.Blue
                    )
                }
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove" else "Add"
                )
            }
        }
    }
}