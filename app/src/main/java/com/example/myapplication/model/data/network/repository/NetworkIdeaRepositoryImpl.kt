package com.example.myapplication.model.data.network.repository

import com.example.myapplication.model.data.network.api.IdeaApiService
import com.example.myapplication.model.data.network.dto.CreateIdeaRequest
import com.example.myapplication.model.data.network.dto.IdeaDto
import javax.inject.Inject

class NetworkIdeaRepositoryImpl @Inject constructor(
    private val apiService: IdeaApiService
) : NetworkIdeaRepository {

    override suspend fun fetchAllIdeas(): Result<List<IdeaDto>> = runCatching {
        val response = apiService.getAllIdeas()
        if (response.isSuccessful) response.body()!!
        else error("Error: ${response.code()}")
    }

    override suspend fun fetchIdeaById(id: Long): Result<IdeaDto> = runCatching {
        val response = apiService.getIdeaById(id)
        if (response.isSuccessful) response.body()!!
        else error("Error: ${response.code()}")
    }

    override suspend fun createIdea(request: CreateIdeaRequest): Result<IdeaDto> = runCatching {
        val response = apiService.createIdea(request)
        if (response.isSuccessful) response.body()!!
        else error("Error: ${response.code()}")
    }

    override suspend fun updateIdea(id: Long, request: CreateIdeaRequest): Result<IdeaDto> = runCatching {
        val response = apiService.updateIdea(id, request)
        if (response.isSuccessful) response.body()!!
        else error("Error: ${response.code()}")
    }

    override suspend fun deleteIdea(id: Long): Result<Unit> = runCatching {
        val response = apiService.deleteIdea(id)
        if (!response.isSuccessful) error("Error: ${response.code()}")
    }
}