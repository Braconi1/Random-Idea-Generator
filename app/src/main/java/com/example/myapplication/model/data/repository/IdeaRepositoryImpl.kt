package com.example.myapplication.model.data.repository

import com.example.myapplication.model.data.local.dao.*
import com.example.myapplication.model.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IdeaRepositoryImpl @Inject constructor(
    private val ideaDao: IdeaDao,
    private val categoryDao: CategoryDao,
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao,
    private val tagDao: TagDao
) : IdeaRepository {

    override fun getAllIdeas(): Flow<List<IdeaEntity>> = ideaDao.getAllIdeas()
    override suspend fun getIdeaById(id: Long): IdeaEntity? = ideaDao.getIdeaById(id)
    override suspend fun insertIdea(idea: IdeaEntity): Long = ideaDao.insertIdea(idea)
    override suspend fun updateIdea(idea: IdeaEntity) = ideaDao.updateIdea(idea)
    override suspend fun deleteIdea(idea: IdeaEntity) = ideaDao.deleteIdea(idea)

    override fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    override suspend fun insertCategory(category: CategoryEntity): Long = categoryDao.insertCategory(category)
    override suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)

    override fun getFavoriteIdeas(userId: Long): Flow<List<IdeaEntity>> = favoriteDao.getFavoriteIdeasByUser(userId)
    override suspend fun addFavorite(favorite: FavoriteEntity) = favoriteDao.addFavorite(favorite)
    override suspend fun removeFavorite(userId: Long, ideaId: Long) = favoriteDao.removeFavoriteById(userId, ideaId)

    override suspend fun insertUser(user: UserEntity): Long = userDao.insertUser(user)
    override suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)

    override fun getTagsForIdea(ideaId: Long): Flow<List<TagEntity>> = tagDao.getTagsForIdea(ideaId)
    override suspend fun insertTag(tag: TagEntity): Long = tagDao.insertTag(tag)
    override suspend fun deleteTag(tag: TagEntity) = tagDao.deleteTag(tag)

    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
}