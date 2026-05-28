package com.example.myapplication.model.data.network.api

import com.example.myapplication.model.data.network.dto.CreateIdeaRequest
import com.example.myapplication.model.data.network.dto.IdeaDto
import retrofit2.Response
import retrofit2.http.*

interface IdeaApiService {

    @GET("posts")
    suspend fun getAllIdeas(): Response<List<IdeaDto>>

    @GET("posts/{id}")
    suspend fun getIdeaById(@Path("id") id: Long): Response<IdeaDto>

    @POST("posts")
    suspend fun createIdea(@Body request: CreateIdeaRequest): Response<IdeaDto>

    @PUT("posts/{id}")
    suspend fun updateIdea(
        @Path("id") id: Long,
        @Body request: CreateIdeaRequest
    ): Response<IdeaDto>

    @DELETE("posts/{id}")
    suspend fun deleteIdea(@Path("id") id: Long): Response<Unit>
}