package com.albertviaplana.chamaasignment.entities

data class Review(
    val authorName: String,
    val authorUrl: String,
    val languageCode: String,
    val profilePhotoUrl: String,
    val rating: Float,
    val text: String,
    val relativeDateDescription: String
)