package com.example.myapplication.model.data.network.dto

data class CreateIdeaRequest(
    val title: String,
    val description: String,
    val category: String
)