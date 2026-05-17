package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.local.entity.UserEntity
import com.example.myapplication.model.data.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Init : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class WelcomeBack(val username: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loggedInUser = MutableStateFlow<UserEntity?>(null)
    val loggedInUser: StateFlow<UserEntity?> = _loggedInUser.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun onEmailChange(value: String) { email.value = value }
    fun onPasswordChange(value: String) { password.value = value }

    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val user = repository.getUserByEmail(email.value)
                if (user != null && user.password == password.value) {
                    _loggedInUser.value = user
                    _uiState.value = LoginUiState.WelcomeBack(user.username)
                } else if (user == null) {
                    _uiState.value = LoginUiState.Error("User not found. Please sign up first.")
                } else {
                    _uiState.value = LoginUiState.Error("Wrong password")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val existing = repository.getUserByEmail(email)
                if (existing != null) {
                    _uiState.value = LoginUiState.Error("User already exists")
                    return@launch
                }
                val newUser = UserEntity(
                    username = username,
                    email = email,
                    password = password
                )
                repository.insertUser(newUser)
                _loggedInUser.value = newUser
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = _loggedInUser.value
                if (user == null) {
                    onError("No user logged in")
                    return@launch
                }
                val updatedUser = user.copy(password = newPassword)
                repository.updateUser(updatedUser)
                _loggedInUser.value = updatedUser
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to change password")
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
        _uiState.value = LoginUiState.Init
        email.value = ""
        password.value = ""
    }

    fun resetState() { _uiState.value = LoginUiState.Init }
}