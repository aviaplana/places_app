package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaasignment.entities.Place

data class NearbyPlacesVM (
    val places: List<PlaceVM>
)

data class PlaceVM(
    val name: String,
    val iconUrl: String,
    val rating: Float,
    val vicinity: String,
    val openStatus: OpenStatus
)

fun Place.toVM() =
    PlaceVM(
        name = name,
        iconUrl = iconUrl,
        rating = rating,
        vicinity = vicinity,
        openStatus = openStatus
    )

sealed class Event
object Loading: Event()
data class Error(val message: String): Event()


sealed class Action
object LoadData: Event()
data class ShowPlace(val position: Int): Event()
