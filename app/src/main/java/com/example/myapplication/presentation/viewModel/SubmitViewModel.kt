package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.local.entity.CategoryEntity
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.model.data.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SubmitUiState {
    object Init : SubmitUiState()
    object Loading : SubmitUiState()
    object Success : SubmitUiState()
    data class Error(val message: String) : SubmitUiState()
}

@HiltViewModel
class SubmitViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubmitUiState>(SubmitUiState.Init)
    val uiState: StateFlow<SubmitUiState> = _uiState.asStateFlow()

    val title = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val cat = MutableStateFlow("")

    fun onTitleChange(value: String) { title.value = value }
    fun onDescriptionChange(value: String) { desc.value = value }
    fun onCategoryChange(value: String) { cat.value = value }

    fun submitIdea() {
        val titleText = title.value.trim()
        val descText = desc.value.trim()
        val catText = cat.value.trim()

        if (titleText.isEmpty() || descText.isEmpty() || catText.isEmpty()) {
            _uiState.value = SubmitUiState.Error("Please fill in all fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = SubmitUiState.Loading
            try {
                val categoryId = repository.insertCategory(CategoryEntity(name = catText))
                repository.insertIdea(
                    IdeaEntity(title = titleText, description = descText, categoryId = categoryId)
                )
                title.value = ""
                desc.value = ""
                cat.value = ""
                _uiState.value = SubmitUiState.Success
            } catch (e: Exception) {
                _uiState.value = SubmitUiState.Error(e.message ?: "Failed to submit")
            }
        }
    }

    fun resetState() { _uiState.value = SubmitUiState.Init }
}