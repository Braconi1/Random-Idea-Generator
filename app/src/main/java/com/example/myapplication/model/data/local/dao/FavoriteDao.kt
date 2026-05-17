package com.example.myapplication.model.data.local.dao

import androidx.room.*
import com.example.myapplication.model.data.local.entity.FavoriteEntity
import com.example.myapplication.model.data.local.entity.IdeaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoritesByUser(userId: Long): Flow<List<FavoriteEntity>>

    @Query("""
        SELECT ideas.* FROM ideas 
        INNER JOIN favorites ON ideas.ideaId = favorites.ideaId 
        WHERE favorites.userId = :userId
    """)
    fun getFavoriteIdeasByUser(userId: Long): Flow<List<IdeaEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND ideaId = :ideaId")
    suspend fun removeFavoriteById(userId: Long, ideaId: Long)
}