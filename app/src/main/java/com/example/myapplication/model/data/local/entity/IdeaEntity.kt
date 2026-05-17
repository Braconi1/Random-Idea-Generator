package com.example.myapplication.model.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ideas",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true)
    val ideaId: Long = 0,
    val title: String,
    val description: String,
    @ColumnInfo(index = true)
    val categoryId: Long = 0
)