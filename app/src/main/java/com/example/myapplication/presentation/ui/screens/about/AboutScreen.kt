package com.example.myapplication.presentation.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.ui.screens.about.components.AboutInfoRow
import com.example.myapplication.presentation.ui.screens.home.components.SectionTitle
import com.example.myapplication.presentation.viewModel.AboutViewModel

// STATEFUL
@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    modifier: Modifier = Modifier
) {
    AboutContent(modifier = modifier)
}

// STATELESS
@Composable
fun AboutContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppDimensions.contentPadding, vertical = AppDimensions.spacingM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))
        Text(
            text = "Idea Generator",
            fontSize = AppDimensions.fontDisplay,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(AppDimensions.spacingXS))
        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(AppDimensions.spacingGiant))
        SectionTitle("About This App")
        Spacer(modifier = Modifier.height(AppDimensions.XL))
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "This app help you find things to do when you are bored. You can save your favorites and add your own ideas too",
                modifier = Modifier.padding(AppDimensions.cardPadding),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))
        SectionTitle("App Features")
        Spacer(modifier = Modifier.height(AppDimensions.spacingL))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(AppDimensions.cardPadding)) {
                AboutInfoRow(label = "Random Ideas", value = "Get random activity suggestions")
                Spacer(modifier = Modifier.height(AppDimensions.XL))
                AboutInfoRow(label = "Favorites", value = "Save ideas you like")
                Spacer(modifier = Modifier.height(AppDimensions.XL))
                AboutInfoRow(label = "History", value = "See what you looked at before")
                Spacer(modifier = Modifier.height(AppDimensions.XL))
                AboutInfoRow(label = "Submit Ideas", value = "Add your own ideas to the app")
            }
        }
        Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))
    }
}