package com.example.apppersonajes.domain.models

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val imageUri: Thumbnail
)