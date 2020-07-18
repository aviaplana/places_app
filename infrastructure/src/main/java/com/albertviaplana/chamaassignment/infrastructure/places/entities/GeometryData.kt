package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.google.gson.annotations.SerializedName


data class GeometryData(
    val location: LocationData
)

data class LocationData(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)

fun LocationData.toDomain() =
    Coordinates(
        latitude = latitude,
        longitude = longitude
    )
