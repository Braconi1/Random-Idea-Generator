package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.local.entity.CategoryEntity
import com.example.myapplication.model.data.local.entity.FavoriteEntity
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.model.data.local.entity.UserEntity
import com.example.myapplication.model.data.repository.IdeaRepository
import com.example.myapplication.model.hardcodedIdeas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    object Init : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val ideas: List<IdeaEntity>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Init)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<List<IdeaEntity>>(emptyList())
    val favorites: StateFlow<List<IdeaEntity>> = _favorites.asStateFlow()

    private val _currentIdea = MutableStateFlow<IdeaEntity?>(null)
    val currentIdea: StateFlow<IdeaEntity?> = _currentIdea.asStateFlow()

    private val currentUserId = 1L

    init {
        viewModelScope.launch {
            ensureUserExists()
            loadIdeas()
            loadFavorites()
        }
    }

    private suspend fun ensureUserExists() {
        val user = repository.getUserByEmail("default@app.com")
        if (user == null) {
            repository.insertUser(
                UserEntity(
                    userId = 1L,
                    username = "default",
                    email = "default@app.com",
                    password = "default"
                )
            )
        }
    }

    private fun loadIdeas() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                repository.getAllIdeas().collect { ideas ->
                    if (ideas.isEmpty()) {
                        seedDatabase()
                    } else {
                        _uiState.value = HomeUiState.Success(ideas)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteIdeas(currentUserId).collect {
                _favorites.value = it
            }
        }
    }

    private suspend fun seedDatabase() {
        hardcodedIdeas.forEach { idea ->
            val categoryId = repository.insertCategory(
                CategoryEntity(name = idea.category)
            )
            repository.insertIdea(
                IdeaEntity(
                    title = idea.title,
                    description = idea.description,
                    categoryId = categoryId
                )
            )
        }
    }

    fun generateRandomIdea() {
        val state = _uiState.value
        if (state is HomeUiState.Success && state.ideas.isNotEmpty()) {
            _currentIdea.value = state.ideas.random()
        }
    }

    fun addToFavorites(idea: IdeaEntity) {
        viewModelScope.launch {
            repository.addFavorite(FavoriteEntity(userId = currentUserId, ideaId = idea.ideaId))
        }
    }

    fun removeFromFavorites(idea: IdeaEntity) {
        viewModelScope.launch {
            repository.removeFavorite(userId = currentUserId, ideaId = idea.ideaId)
        }
    }
}