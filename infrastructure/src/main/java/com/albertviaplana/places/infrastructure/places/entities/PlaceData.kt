package com.albertviaplana.places.infrastructure.places.entities

import com.albertviaplana.places.entities.*
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
    val openingHours: OpeningHoursData?,
    val status: BusinessStatus?,
    val priceLevel: Int,
    val photos: List<PhotoData>?,
    val types: List<String>
)

fun PlaceData.toDomain() =
    Place(
        id = id,
        name = name,
        vicinity = vicinity,
        iconUrl = icon,
        rating = rating,
        location = geometry.location.toDomain(),
        openStatus = openingHours.getOpenStatus(),
        status = status,
        priceLevel = PriceLevel.getByValue(priceLevel),
        photos = photos.orEmpty().map { it.toDomain() },
        types = types.mapNotNull { mapPlaceType(it) }
    )
