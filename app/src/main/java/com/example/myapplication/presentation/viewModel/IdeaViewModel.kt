package com.example.myapplication.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.myapplication.model.Idea
import com.example.myapplication.model.hardcodedIdeas

class IdeaViewModel : ViewModel() {
    val currentIdea = MutableStateFlow<Idea?>(null)

    val title = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val cat = MutableStateFlow("")

    val submitSuccess = MutableStateFlow(false)
    val err = MutableStateFlow<String?>(null)

    val ideas = MutableStateFlow<List<Idea>>(emptyList())
    val favorites = MutableStateFlow<List<Idea>>(emptyList())

    private val ideaSuggestions = hardcodedIdeas

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