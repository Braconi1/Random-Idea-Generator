package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.model.data.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FavoritesUiState {
    object Init : FavoritesUiState()
    object Loading : FavoritesUiState()
    data class Success(val ideas: List<IdeaEntity>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Init)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val currentUserId = 1L

    init { loadFavorites() }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            try {
                repository.getFavoriteIdeas(currentUserId).collect { ideas ->
                    _uiState.value = FavoritesUiState.Success(ideas)
                }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun removeFromFavorites(idea: IdeaEntity) {
        viewModelScope.launch {
            repository.removeFavorite(userId = currentUserId, ideaId = idea.ideaId)
        }
    }
}