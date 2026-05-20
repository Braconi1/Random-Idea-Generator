package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AboutUiState {
    object Init : AboutUiState()
    object Loading : AboutUiState()
    object Success : AboutUiState()
    data class Error(val message: String) : AboutUiState()
}

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AboutUiState>(AboutUiState.Init)
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = AboutUiState.Success
    }
}