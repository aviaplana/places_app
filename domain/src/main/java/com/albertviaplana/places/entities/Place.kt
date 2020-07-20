package com.albertviaplana.places.entities

data class Place(
    val id: String,
    val name: String,
    val vicinity: String,
    val iconUrl: String,
    val rating: Float,
    val location: Coordinates,
    val openStatus: OpenStatus,
    val status: BusinessStatus?,
    val priceLevel: PriceLevel,
    val photos: List<Photo>,
    val types: List<PlaceType>
)