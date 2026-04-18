package com.example.myapplication.model

data class Idea(
    val id: Int,
    val title: String,
    val description: String,
    val category: String = ""
)