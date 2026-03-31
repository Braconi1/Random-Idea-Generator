package com.example.myapplication.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.screens.about.components.AboutInfoRow
import com.example.myapplication.screens.home.components.SectionTitle

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Idea Generator",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(27.dp))

        SectionTitle("About This App")
        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "This app help you find things to do when you are bored. You can save your favorites and add your own ideas too",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("App Features")
        Spacer(modifier = Modifier.height(10.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                AboutInfoRow(label = "Random Ideas", value = "Get random activity suggestions")
                Spacer(modifier = Modifier.height(12.dp))
                AboutInfoRow(label = "Favorites", value = "Save ideas you like")
                Spacer(modifier = Modifier.height(12.dp))
                AboutInfoRow(label = "History", value = "See what you looked at before")
                Spacer(modifier = Modifier.height(12.dp))
                AboutInfoRow(label = "Submit Ideas", value = "Add your own ideas to the app")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}