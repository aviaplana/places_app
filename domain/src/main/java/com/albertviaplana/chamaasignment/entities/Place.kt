package com.albertviaplana.chamaasignment.entities

data class Place(
    val id: String,
    val name: String,
    val vicinity: String,
    val iconUrl: String,
    val location: Coordinates,
    val isOpen: Boolean,
    val status: BusinessStatus?,
    val priceLevel: PriceLevel,
    val photos: List<Photo>,
    val types: List<PlaceType>
)