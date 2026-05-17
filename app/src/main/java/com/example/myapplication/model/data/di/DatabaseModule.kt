package com.example.myapplication.model.data.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.model.data.local.dao.*
import com.example.myapplication.model.data.local.db.AppDatabase
import com.example.myapplication.model.data.local.util.DatabaseConstants
import com.example.myapplication.model.data.repository.IdeaRepository
import com.example.myapplication.model.data.repository.IdeaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideIdeaDao(database: AppDatabase): IdeaDao = database.ideaDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase): TagDao = database.tagDao()

    @Provides
    @Singleton
    fun provideIdeaRepository(impl: IdeaRepositoryImpl): IdeaRepository = impl
}