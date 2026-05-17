package com.example.myapplication.model.data.repository.mappers

import com.example.myapplication.model.Idea
import com.example.myapplication.model.data.local.entity.IdeaEntity

fun IdeaEntity.toDomain(): Idea {
    return Idea(
        id = ideaId.toInt(),
        title = title,
        description = description,
        category = categoryId.toString()
    )
}

fun Idea.toEntity(categoryId: Long = 0L): IdeaEntity {
    return IdeaEntity(
        ideaId = id.toLong(),
        title = title,
        description = description,
        categoryId = categoryId
    )
}

fun List<IdeaEntity>.toDomainList(): List<Idea> = map { it.toDomain() }
fun List<Idea>.toEntityList(): List<IdeaEntity> = map { it.toEntity() }