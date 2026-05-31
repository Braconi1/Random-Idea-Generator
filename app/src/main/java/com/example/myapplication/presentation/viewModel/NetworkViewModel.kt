package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.network.dto.CreateIdeaRequest
import com.example.myapplication.model.data.network.dto.IdeaDto
import com.example.myapplication.model.data.network.repository.NetworkIdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NetworkUiState {
    object Idle : NetworkUiState()
    object Loading : NetworkUiState()
    data class Success(val ideas: List<IdeaDto>) : NetworkUiState()
    data class Error(val message: String) : NetworkUiState()
}

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val repository: NetworkIdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetworkUiState>(NetworkUiState.Idle)
    val uiState: StateFlow<NetworkUiState> = _uiState

    fun fetchIdeas() {
        viewModelScope.launch {
            _uiState.value = NetworkUiState.Loading
            repository.fetchAllIdeas()
                .onSuccess { _uiState.value = NetworkUiState.Success(it) }
                .onFailure { _uiState.value = NetworkUiState.Error(it.message ?: "Unknown") }
        }
    }

    fun createIdea(title: String, description: String, category: String) {
        viewModelScope.launch {
            _uiState.value = NetworkUiState.Loading
            repository.createIdea(CreateIdeaRequest(title, description, category))
                .onSuccess { fetchIdeas() }
                .onFailure { _uiState.value = NetworkUiState.Error(it.message ?: "Unknown") }
        }
    }

    fun deleteIdea(id: Long) {
        viewModelScope.launch {
            repository.deleteIdea(id)
                .onSuccess { fetchIdeas() }
                .onFailure { _uiState.value = NetworkUiState.Error(it.message ?: "Unknown") }
        }
    }
}