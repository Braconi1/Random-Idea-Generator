package com.example.myapplication.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Idea(
    val id: Int,
    val title: String,
    val description: String,
    val category: String = ""
)

class IdeaViewModel : ViewModel() {
    val currentIdea = MutableStateFlow<Idea?>(null)

    val title = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val cat = MutableStateFlow("")

    val submitSuccess = MutableStateFlow(false)
    val err = MutableStateFlow<String?>(null)

    val ideas = MutableStateFlow<List<Idea>>(emptyList())
    val favorites = MutableStateFlow<List<Idea>>(emptyList())

    //LISTA IDEJA ZA DODAT JOS verzija 1.0
    private val ideaSuggestions = listOf(
        Idea(1, "Learn to juggle", "Start with 3 balls and practice daily", "Activities"),
        Idea(2, "Meditate for 10 minutes", "Find a quiet spot and focus on breathing", "Health"),
        Idea(3, "Read a book", "Choose a genre you enjoy and read for 30 minutes", "Learning"),
        Idea(4, "Learn a new language", "Use Duolingo for 15 minutes daily", "Learning"),
        Idea(5, "Go for a walk", "Enjoy nature and get some fresh air", "Health"),
        Idea(6, "Cook a new recipe", "Try something you've never made before", "Activities"),
        Idea(7, "Write in a journal", "Express your thoughts and feelings", "Creativity"),
        Idea(8, "Learn to play an instrument", "Start with basic chords on guitar or piano", "Learning"),
        Idea(9, "Do a random act of kindness", "Make someone's day better", "Social"),
        Idea(10, "Learn a magic trick", "Impress your friends with a simple illusion", "Activities")
    )

    fun generateRandomIdea() {
        val randomIdea = ideaSuggestions.random()
        currentIdea.value = randomIdea
    }

    fun onTitleChange(newTitle: String) {
        title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        desc.value = newDescription
    }

    fun onCategoryChange(newCategory: String) {
        cat.value = newCategory
    }

    fun submitIdea() {
        val titleText = title.value.trim()
        val descText = desc.value.trim()
        val catText = cat.value.trim()

        if (titleText.isEmpty() || descText.isEmpty() || catText.isEmpty()) {
            err.value = "Please fill in all fields"
            return
        }

        try {
            val newIdea = Idea(
                id = (ideas.value.size + 1),
                title = titleText,
                description = descText,
                category = catText
            )
            ideas.value = ideas.value + newIdea

            title.value = ""
            desc.value = ""
            cat.value = ""
            submitSuccess.value = true
            err.value = null
        } catch (e: Exception) {
            err.value = "Failed to submit idea: ${e.message}"
        }
    }

    fun clearSubmitSuccess() {
        submitSuccess.value = false
    }

    fun addToFavorites(idea: Idea) {
        if (!favorites.value.any { it.id == idea.id }) {
            favorites.value = favorites.value + idea
        }
    }

    fun removeFromFavorites(idea: Idea) {
        favorites.value = favorites.value.filter { it.id != idea.id }
    }
}