
package com.example.myapplication.presentation.theme.ui

import androidx.lifecycle.ViewModel
import com.example.myapplication.Idea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IdeaViewModel : ViewModel() {

    private val allIdeas = listOf(
        Idea(1, "Go for a walk", "Take a 30 min walk outside", "Activities"),
        Idea(2, "Try meditation", "Meditate for 10 minutes", "Activities"),
        Idea(3, "Call a friend", "Catch up with someone you miss", "Activities"),
        Idea(4, "Draw something", "Sketch anything around you", "Activities"),
        Idea(5, "Read a book", "Pick any book and read 20 pages", "Activities"),
        Idea(6, "Cook a new recipe", "Try something you never cooked", "Activities"),
        Idea(7, "Watch a documentary", "Learn something new", "Activities"),
        Idea(8, "Do a workout", "20 min home workout", "Activities")
    )

    private val _currentIdea = MutableStateFlow<Idea?>(null)
    val currentIdea: StateFlow<Idea?> = _currentIdea

    private val _favorites = MutableStateFlow<List<Idea>>(emptyList())
    val favorites: StateFlow<List<Idea>> = _favorites

    private val _history = MutableStateFlow<List<Idea>>(emptyList())
    val history: StateFlow<List<Idea>> = _history

    fun generateRandomIdea() {
        val idea = allIdeas.random()
        _currentIdea.value = idea
        _history.value = listOf(idea) + _history.value
    }

    fun addToFavorites(idea: Idea) {
        if (!_favorites.value.contains(idea)) {
            _favorites.value = _favorites.value + idea
        }
    }

    fun removeFromFavorites(idea: Idea) {
        _favorites.value = _favorites.value.filter { it.id != idea.id }
    }
}
