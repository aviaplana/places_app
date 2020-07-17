package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaasignment.entities.Place

data class NearbyPlacesVM (
    val places: List<PlaceVM>,
    val isLoading: Boolean
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
data class ShowError(val message: String): Event()
object ShowDetails: Event()

sealed class Action
object LoadData: Action()
object ScrollBottom: Action()
data class ClickedPlace(val position: Int): Action()
