package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.local.entity.FavoriteEntity
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.model.data.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailUiState {
    object Init : DetailUiState()
    object Loading : DetailUiState()
    data class Success(val idea: IdeaEntity) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Init)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val currentUserId = 1L

    fun loadIdea(ideaId: Long) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val idea = repository.getIdeaById(ideaId)
                if (idea != null) {
                    _uiState.value = DetailUiState.Success(idea)
                    checkFavorite(ideaId)
                } else {
                    _uiState.value = DetailUiState.Error("Idea not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun checkFavorite(ideaId: Long) {
        viewModelScope.launch {
            repository.getFavoriteIdeas(currentUserId).collect { favorites ->
                _isFavorite.value = favorites.any { it.ideaId == ideaId }
            }
        }
    }

    fun toggleFavorite(idea: IdeaEntity) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                repository.removeFavorite(userId = currentUserId, ideaId = idea.ideaId)
            } else {
                repository.addFavorite(FavoriteEntity(userId = currentUserId, ideaId = idea.ideaId))
            }
        }
    }
}