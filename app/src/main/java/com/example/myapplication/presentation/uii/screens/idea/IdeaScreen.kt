package com.example.myapplication.presentation.uii.screens.idea

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.viewModel.IdeaViewModel
import com.example.myapplication.presentation.uii.screens.home.components.SectionTitle
import com.example.myapplication.presentation.uii.screens.idea.components.ValidatedTextField

@Composable
fun IdeaScreen(
    viewModel: IdeaViewModel,
    modifier: Modifier = Modifier
) {
    val title by viewModel.title.collectAsState()
    val descText by viewModel.desc.collectAsState()
    val catText by viewModel.cat.collectAsState()
    val submitSuccess by viewModel.submitSuccess.collectAsState()
    val submitError by viewModel.err.collectAsState()

    var showSuccessMsg by remember { mutableStateOf(false) }

    if (submitSuccess) {
        showSuccessMsg = true
        viewModel.clearSubmitSuccess()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppDimensions.contentPadding, vertical = AppDimensions.spacingM)
        ) {
            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            Text(text = "Submit Your Idea", fontSize = AppDimensions.fontXXL, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(AppDimensions.spacingS))

            Text(
                text = "Fill in all fields to add a new idea to the list.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimensions.spacingHuge))

            SectionTitle(text = "Idea Details")

            Spacer(modifier = Modifier.height(AppDimensions.spacingXXXL))

            ValidatedTextField(
                value = title,
                onValueChange = viewModel::onTitleChange,
                label = "Title",
                placeholder = "e.g. Learn to juggle",
                isError = submitError != null && title.isBlank()
            )

            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            ValidatedTextField(
                value = descText,
                onValueChange = viewModel::onDescriptionChange,
                label = "Description",
                placeholder = "e.g. Watch a tutorial and practice daily",
                isError = submitError != null && descText.isBlank(),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

            ValidatedTextField(
                value = catText,
                onValueChange = viewModel::onCategoryChange,
                label = "Category",
                placeholder = "e.g. Activities, Learning, Health",
                isError = submitError != null && catText.isBlank()
            )

            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            if (submitError != null) {
                Text(
                    text = submitError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showSuccessMsg) {
                Spacer(modifier = Modifier.height(AppDimensions.spacingM))
                Text(
                    text = "Idea submitted successfully! 🎉",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.spacingMassive))

            var isFormValid = false
            if (title.isNotBlank()) {
                if (descText.isNotBlank()) {
                    if (catText.isNotBlank()) {
                        isFormValid = true
                    }
                }
            }

            Button(
                onClick = { viewModel.submitIdea() },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(AppDimensions.buttonHeight)
            ) {
                Text("Submit Idea", fontSize = AppDimensions.fontMedium)
            }

            if (!isFormValid) {
                Spacer(modifier = Modifier.height(AppDimensions.XL))
                Text(
                    text = "Please fill in all fields to enable submit.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.spacingM))
        }
    }
}