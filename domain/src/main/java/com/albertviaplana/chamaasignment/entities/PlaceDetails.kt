package com.albertviaplana.chamaasignment.entities

data class PlaceDetails(
    val id: String,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val internationalPhoneNumber: String,
    val iconUrl: String,
    val status: BusinessStatus?,
    val isOpen: Boolean,
    val openHours: List<String>,
    val rating: Float,
    val priceLevel: PriceLevel,
    val reviews: List<Review>,
    val vicinity: String,
    val url: String,
    val website: String,
    val location: Coordinates,
    val photos: List<Photo>,
    val types: List<PlaceType>
)