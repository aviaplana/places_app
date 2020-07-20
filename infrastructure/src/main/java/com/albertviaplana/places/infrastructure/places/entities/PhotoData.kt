package com.albertviaplana.places.infrastructure.places.entities

import com.albertviaplana.places.entities.Photo
import com.albertviaplana.places.infrastructure.BuildConfig
import com.google.gson.annotations.SerializedName

data class PhotoData(
    @SerializedName("photo_reference") val reference: String,
    val width: Int,
    val height: Int
)

fun PhotoData.toDomain() =
    Photo(
        url = "${BuildConfig.PLACES_API_URL}place/photo?maxwidth=640&photoreference=$reference&key=${BuildConfig.PLACES_API_KEY}",
        width = width,
        height = height
    )