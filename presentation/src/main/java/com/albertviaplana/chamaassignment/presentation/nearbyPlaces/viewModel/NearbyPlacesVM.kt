package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaasignment.entities.Place

data class NearbyPlacesVM (
    val places: List<PlaceVM>,
    val isLoading: Boolean
)

data class PlaceVM(
    val id: String,
    val name: String,
    val iconUrl: String,
    val rating: Float,
    val vicinity: String,
    val openStatus: OpenStatus
)

fun Place.toVM() =
    PlaceVM(
        id = id,
        name = name,
        iconUrl = iconUrl,
        rating = rating,
        vicinity = vicinity,
        openStatus = openStatus
    )

sealed class NearbyEvent
data class ShowError(val message: String): NearbyEvent()
data class ShowDetails(val id: String): NearbyEvent()

sealed class NearbyAction
object LoadData: NearbyAction()
object ScrollBottom: NearbyAction()
data class ClickedPlace(val position: Int): NearbyAction()
