package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.Review

data class ReviewData(
    val authorName: String,
    val authorUrl: String?,
    val language: String?,
    val profilePhotoUrl: String,
    val rating: Float,
    val relativeTimeDescription: String,
    val text: String?
)

fun ReviewData.toDomain() =
    Review(
        authorName = authorName,
        authorUrl = authorUrl.orEmpty(),
        profilePhotoUrl = profilePhotoUrl,
        rating = rating,
        text = text.orEmpty(),
        languageCode = language.orEmpty(),
        relativeDateDescription = relativeTimeDescription
    )