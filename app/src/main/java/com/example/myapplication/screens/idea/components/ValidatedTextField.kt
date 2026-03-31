package com.example.myapplication.screens.idea.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isError: Boolean    = false,
    singleLine: Boolean = true,
    minLines: Int       = 1,
    modifier: Modifier  = Modifier
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label) },
        placeholder   = { Text(placeholder) },
        isError       = isError,
        singleLine    = singleLine,
        minLines      = minLines,
        modifier      = modifier.fillMaxWidth(),
        supportingText = if (isError) {
            { Text("$label cannot be empty", color = MaterialTheme.colorScheme.error) }
        } else null
    )
}