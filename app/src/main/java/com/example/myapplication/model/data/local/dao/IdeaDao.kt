package com.example.myapplication.model.data.local.dao

import androidx.room.*
import com.example.myapplication.model.data.local.entity.IdeaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Query("SELECT * FROM ideas")
    fun getAllIdeas(): Flow<List<IdeaEntity>>

    @Query("SELECT * FROM ideas WHERE categoryId = :categoryId")
    fun getIdeasByCategory(categoryId: Long): Flow<List<IdeaEntity>>

    @Query("SELECT * FROM ideas WHERE ideaId = :id")
    suspend fun getIdeaById(id: Long): IdeaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: IdeaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdeas(ideas: List<IdeaEntity>)

    @Update
    suspend fun updateIdea(idea: IdeaEntity)

    @Delete
    suspend fun deleteIdea(idea: IdeaEntity)

    @Query("DELETE FROM ideas")
    suspend fun deleteAllIdeas()
}