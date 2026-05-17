package com.example.myapplication.presentation.ui.screens.idea

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.presentation.AppDimensions
import com.example.myapplication.presentation.ui.screens.home.components.SectionTitle
import com.example.myapplication.presentation.ui.screens.idea.components.ValidatedTextField
import com.example.myapplication.presentation.viewModel.SubmitUiState
import com.example.myapplication.presentation.viewModel.SubmitViewModel

// STATEFUL
@Composable
fun IdeaScreen(
    viewModel: SubmitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val title by viewModel.title.collectAsState()
    val desc by viewModel.desc.collectAsState()
    val cat by viewModel.cat.collectAsState()

    IdeaContent(
        uiState = uiState,
        title = title,
        desc = desc,
        cat = cat,
        onTitleChange = viewModel::onTitleChange,
        onDescChange = viewModel::onDescriptionChange,
        onCatChange = viewModel::onCategoryChange,
        onSubmit = viewModel::submitIdea,
        onResetState = viewModel::resetState,
        modifier = modifier
    )
}

// STATELESS
@Composable
fun IdeaContent(
    uiState: SubmitUiState,
    title: String,
    desc: String,
    cat: String,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onCatChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResetState: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isError = uiState is SubmitUiState.Error
    val isSuccess = uiState is SubmitUiState.Success
    val errorMsg = if (isError) (uiState as SubmitUiState.Error).message else null
    val isFormValid = title.isNotBlank() && desc.isNotBlank() && cat.isNotBlank()

    LaunchedEffect(isSuccess) {
        if (isSuccess) onResetState()
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
                onValueChange = onTitleChange,
                label = "Title",
                placeholder = "e.g. Learn to juggle",
                isError = isError && title.isBlank()
            )
            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            ValidatedTextField(
                value = desc,
                onValueChange = onDescChange,
                label = "Description",
                placeholder = "e.g. Watch a tutorial and practice daily",
                isError = isError && desc.isBlank(),
                singleLine = false,
                minLines = 3
            )
            Spacer(modifier = Modifier.height(AppDimensions.spacingXXL))

            ValidatedTextField(
                value = cat,
                onValueChange = onCatChange,
                label = "Category",
                placeholder = "e.g. Activities, Learning, Health",
                isError = isError && cat.isBlank()
            )
            Spacer(modifier = Modifier.height(AppDimensions.spacingL))

            if (errorMsg != null) {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (isSuccess) {
                Spacer(modifier = Modifier.height(AppDimensions.spacingM))
                Text(
                    text = "Idea submitted successfully! 🎉",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.spacingMassive))

            Button(
                onClick = onSubmit,
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