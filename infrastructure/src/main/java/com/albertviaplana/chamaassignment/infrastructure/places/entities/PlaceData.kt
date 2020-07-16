package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.*
import com.google.gson.annotations.SerializedName
import java.lang.Exception
import java.util.*

data class PlaceData(
    @SerializedName("place_id") val id: String,
    val name: String,
    val vicinity: String,
    val icon: String,
    val rating: Float,
    val geometry: GeometryData,
    val openingHours: OpeningHoursData,
    val status: BusinessStatus?,
    val priceLevel: Int,
    val photos: List<PhotoData>?,
    val types: List<String>
)

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

data class OpeningHoursData(
    val isOpen: Boolean
)

fun OpeningHoursData?.toDomain() =
    this?.let {
        if (it.isOpen) OpenStatus.OPEN
        else OpenStatus.CLOSED
    } ?: OpenStatus.UNKNOWN

fun mapPlaceType(type: String) =
    try {
        PlaceType.valueOf(
            type.toUpperCase(Locale.ROOT)
        )
    } catch(e: Exception) {
        null
    }

fun PlaceData.toDomain() =
    Place(
        id = id,
        name = name,
        vicinity = vicinity,
        iconUrl = icon,
        rating = rating,
        location = geometry.location.toDomain(),
        openStatus = openingHours.toDomain(),
        status = status,
        priceLevel = PriceLevel.getByValue(priceLevel),
        photos = photos.orEmpty().map { it.toDomain() },
        types = types.mapNotNull { mapPlaceType(it) }
    )
