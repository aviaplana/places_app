package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceType
import java.util.*

data class NearbyPlacesVM (
    val places: List<PlaceVM>,
    val isLoading: Boolean,
    val filters: FiltersVM
)

data class PlaceVM(
    val id: String,
    val name: String,
    val iconUrl: String,
    val rating: Float,
    val vicinity: String,
    val openStatus: OpenStatus
)

data class FiltersVM(
    val radiusProgress: Int,
    val type: String,
    val onlyOpenNow: Boolean
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

@ExperimentalStdlibApi
fun PlaceType.toVM(): String {
    val locale = Locale.getDefault()
    return name.toLowerCase(locale)
        .capitalize(locale)
        .replace("_", " ")
}

sealed class NearbyEvent
object ShowFilters: NearbyEvent()
object ShowFiltersButton: NearbyEvent()
object HideFiltersButton: NearbyEvent()
object CheckPermissions: NearbyEvent()
data class UpdateRadiusLabel(val radius: Int): NearbyEvent()
data class SetTypeFilterOptions(val placeTypes: List<String>): NearbyEvent()
data class ShowError(val message: String): NearbyEvent()
data class ShowDetails(val id: String): NearbyEvent()

sealed class NearbyAction
object Created: NearbyAction()
object ViewCreated: NearbyAction()
data class FiltersDismissed(val progress: Int, val onlyOpenNow: Boolean, val type: String): NearbyAction()
data class RadiusChanged(val radius: Int): NearbyAction()
object PermissionsGranted: NearbyAction()
object PermissionsDenied: NearbyAction()
object ScrollBottom: NearbyAction()
object ScrollDown: NearbyAction()
object ScrollUp: NearbyAction()
object ClickedFiltersButton: NearbyAction()
data class ClickedPlace(val position: Int): NearbyAction()
