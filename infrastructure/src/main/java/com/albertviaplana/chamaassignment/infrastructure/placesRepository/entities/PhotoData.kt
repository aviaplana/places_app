package com.albertviaplana.chamaassignment.infrastructure.placesRepository.entities

import com.albertviaplana.chamaasignment.entities.Photo
import com.google.gson.annotations.SerializedName


data class PhotoData(
    @SerializedName("photo_reference") val reference: String,
    val width: Int,
    val height: Int
)

fun PhotoData.toDomain() =
    Photo(
        reference = reference,
        width = width,
        height = height
    )