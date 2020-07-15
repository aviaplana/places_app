package com.albertviaplana.chamaassignment.infrastructure.placesRepository.entities

import com.albertviaplana.chamaasignment.entities.*
import com.google.gson.annotations.SerializedName

data class PlaceData(
    val placeId: String,
    val name: String,
    val vicinity: String,
    val icon: String,
    val geometry: GeometryData,
    val openingHours: OpeningHoursData,
    val status: BusinessStatus?,
    val priceLevel: Int,
    val photos: List<PhotoData>,
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

fun PlaceData.toDomain() =
    Place(
        id = placeId,
        name = name,
        vicinity = vicinity,
        iconUrl = icon,
        location = geometry.location.toDomain(),
        isOpen = openingHours.isOpen,
        status = status,
        priceLevel = PriceLevel.getByValue(priceLevel),
        photos = photos.map { it.toDomain() },
        types = types.map { PlaceType.valueOf(it) }
    )
