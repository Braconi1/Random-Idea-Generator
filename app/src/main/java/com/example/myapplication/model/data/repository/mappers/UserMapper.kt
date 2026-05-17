package com.example.myapplication.model.data.repository.mappers

import com.example.myapplication.model.data.local.entity.UserEntity

data class UserDomain(
    val id: Long,
    val username: String,
    val email: String
)

fun UserEntity.toDomain(): UserDomain {
    return UserDomain(
        id = userId,
        username = username,
        email = email
    )
}