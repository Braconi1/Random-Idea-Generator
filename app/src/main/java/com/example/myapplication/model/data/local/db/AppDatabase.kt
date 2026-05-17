package com.example.myapplication.model.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.data.local.dao.*
import com.example.myapplication.model.data.local.entity.*

@Database(
    entities = [
        IdeaEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        FavoriteEntity::class,
        TagEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ideaDao(): IdeaDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "idea_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}