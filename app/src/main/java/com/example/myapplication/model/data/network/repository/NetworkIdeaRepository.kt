package com.example.myapplication.model.data.network.repository

import com.example.myapplication.model.data.network.dto.CreateIdeaRequest
import com.example.myapplication.model.data.network.dto.IdeaDto

interface NetworkIdeaRepository {
    suspend fun fetchAllIdeas(): Result<List<IdeaDto>>
    suspend fun fetchIdeaById(id: Long): Result<IdeaDto>
    suspend fun createIdea(request: CreateIdeaRequest): Result<IdeaDto>
    suspend fun updateIdea(id: Long, request: CreateIdeaRequest): Result<IdeaDto>
    suspend fun deleteIdea(id: Long): Result<Unit>
}