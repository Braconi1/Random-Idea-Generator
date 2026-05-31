package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.repository.FirebaseIdea
import com.example.myapplication.model.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FirebaseUiState {
    object Idle : FirebaseUiState()
    object Loading : FirebaseUiState()
    object Success : FirebaseUiState()
    data class Error(val message: String) : FirebaseUiState()
}

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FirebaseUiState>(FirebaseUiState.Idle)
    val uiState: StateFlow<FirebaseUiState> = _uiState

    private val _ideas = MutableStateFlow<List<FirebaseIdea>>(emptyList())
    val ideas: StateFlow<List<FirebaseIdea>> = _ideas

    private val _isLoggedIn = MutableStateFlow(repository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        if (repository.isLoggedIn()) {
            loadIdeas()
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = FirebaseUiState.Loading
            repository.signUp(email, password)
                .onSuccess {
                    _isLoggedIn.value = true
                    _uiState.value = FirebaseUiState.Success
                    loadIdeas()
                }
                .onFailure {
                    _uiState.value = FirebaseUiState.Error(it.message ?: "Sign up failed")
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = FirebaseUiState.Loading
            repository.signIn(email, password)
                .onSuccess {
                    _isLoggedIn.value = true
                    _uiState.value = FirebaseUiState.Success
                    loadIdeas()
                }
                .onFailure {
                    _uiState.value = FirebaseUiState.Error(it.message ?: "Sign in failed")
                }
        }
    }

    fun signOut() {
        repository.signOut()
        _isLoggedIn.value = false
        _ideas.value = emptyList()
        _uiState.value = FirebaseUiState.Idle
    }

    fun saveIdea(title: String, description: String, category: String) {
        viewModelScope.launch {
            _uiState.value = FirebaseUiState.Loading
            repository.saveIdea(title, description, category)
                .onSuccess { _uiState.value = FirebaseUiState.Success }
                .onFailure { _uiState.value = FirebaseUiState.Error(it.message ?: "Failed to save") }
        }
    }

    fun deleteIdea(ideaId: String) {
        viewModelScope.launch {
            repository.deleteIdea(ideaId)
                .onFailure { _uiState.value = FirebaseUiState.Error(it.message ?: "Failed to delete") }
        }
    }

    fun resetState() {
        _uiState.value = FirebaseUiState.Idle
    }

    private fun loadIdeas() {
        viewModelScope.launch {
            repository.getIdeas().collect {
                _ideas.value = it
            }
        }
    }
}