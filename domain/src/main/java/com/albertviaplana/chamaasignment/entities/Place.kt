package com.albertviaplana.chamaasignment.entities

data class Place(
    val id: String,
    val reference: String,
    val name: String,
    val vicinity: String,
    val iconUrl: String,
    val location: Coordinates,
    val photos: List<Photo>,
    val types: List<PlaceType>,
)
