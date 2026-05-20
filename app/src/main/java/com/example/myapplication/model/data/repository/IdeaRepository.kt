package com.example.myapplication.model.data.repository

import com.example.myapplication.model.data.local.entity.CategoryEntity
import com.example.myapplication.model.data.local.entity.FavoriteEntity
import com.example.myapplication.model.data.local.entity.IdeaEntity
import com.example.myapplication.model.data.local.entity.TagEntity
import com.example.myapplication.model.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {

    fun getAllIdeas(): Flow<List<IdeaEntity>>
    suspend fun getIdeaById(id: Long): IdeaEntity?
    suspend fun insertIdea(idea: IdeaEntity): Long
    suspend fun updateIdea(idea: IdeaEntity)
    suspend fun deleteIdea(idea: IdeaEntity)

    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun insertCategory(category: CategoryEntity): Long
    suspend fun deleteCategory(category: CategoryEntity)

    fun getFavoriteIdeas(userId: Long): Flow<List<IdeaEntity>>
    suspend fun addFavorite(favorite: FavoriteEntity)
    suspend fun removeFavorite(userId: Long, ideaId: Long)

    suspend fun insertUser(user: UserEntity): Long
    suspend fun getUserByEmail(email: String): UserEntity?

    fun getTagsForIdea(ideaId: Long): Flow<List<TagEntity>>
    suspend fun insertTag(tag: TagEntity): Long
    suspend fun deleteTag(tag: TagEntity)

    suspend fun updateUser(user: UserEntity)
}