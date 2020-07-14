package com.albertviaplana.chamaasignment.entities

import java.util.*

data class Review(
    val authorName: String,
    val authorUrl: String,
    val languageCode: String,
    val profilePhotoUrl: String,
    val rating: Float,
    val text: String,
    val date: Date,
    val relativeDateDescription: String
)