package com.example.myapplication.model.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class FirebaseIdea(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val userId: String = ""
)

class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        Unit
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    fun signOut() = auth.signOut()

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun isLoggedIn(): Boolean = auth.currentUser != null

    suspend fun saveIdea(title: String, description: String, category: String): Result<Unit> = runCatching {
        val idea = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "userId" to (getCurrentUserId() ?: "")
        )
        firestore.collection("ideas").add(idea).await()
        Unit
    }

    suspend fun deleteIdea(ideaId: String): Result<Unit> = runCatching {
        firestore.collection("ideas").document(ideaId).delete().await()
        Unit
    }

    fun getIdeas(): Flow<List<FirebaseIdea>> = callbackFlow {
        val listener = firestore.collection("ideas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val ideas = snapshot?.documents?.mapNotNull { doc ->
                    val title = doc.getString("title") ?: ""
                    val description = doc.getString("description") ?: ""
                    val category = doc.getString("category") ?: ""
                    val userId = doc.getString("userId") ?: ""
                    FirebaseIdea(id = doc.id, title = title, description = description, category = category, userId = userId)
                } ?: emptyList()
                trySend(ideas)
            }
        awaitClose { listener.remove() }
    }
}