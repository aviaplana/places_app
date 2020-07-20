package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import androidx.lifecycle.viewModelScope
import com.albertviaplana.chamaasignment.PlacesService
import com.albertviaplana.chamaasignment.entities.PlaceType
import com.albertviaplana.chamaassignment.presentation.common.viewModel.BaseViewModel
import com.github.kittinunf.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class NearbyPlacesViewModel(private val placesService: PlacesService): BaseViewModel<NearbyPlacesVM, NearbyEvent>() {
    private val placeTypeFilters = listOf(PlaceType.BAR, PlaceType.CAFE, PlaceType.RESTAURANT)
    @ExperimentalStdlibApi
    override val _state: MutableStateFlow<NearbyPlacesVM> =
        MutableStateFlow(NearbyPlacesVM(
            places = listOf(),
            isLoading = false,
            filters = FiltersVM(
                radiusProgress = 0,
                onlyOpenNow = false,
                type = placeTypeFilters.first().toVM()
            )
        ))

    var hasMoreResults= false

    @ExperimentalStdlibApi
    infix fun reduce(action: NearbyAction) {
        println("ACTION: ${action.javaClass.simpleName}")

        when(action) {
            is RadiusChanged -> sendUpdateRadiusLabel(action.radius)
            is FiltersDismissed -> filtersDismissed(action.progress, action.onlyOpenNow, action.type)
            is PermissionsGranted -> loadInitialData()
            is Created -> CheckPermissions.sendScoped()
            is ViewCreated -> sendSetTypeFilterOptions()
            is PermissionsDenied -> showPermissionsDeniedError()
            is ScrollBottom -> loadNextPage()
            is ScrollUp -> ShowFiltersButton.sendScoped()
            is ScrollDown -> HideFiltersButton.sendScoped()
            is ClickedPlace -> handleClickedPlace(action.position)
            is ClickedFiltersButton -> showFilters()
        }
    }

    @ExperimentalStdlibApi
    private fun sendSetTypeFilterOptions() {
        val placeTypes = placeTypeFilters.map { it.toVM() }
        SetTypeFilterOptions(placeTypes).sendScoped()
    }

    private fun filtersDismissed(progress: Int, onlyOpenNow: Boolean, type: String) {
        if (filtersChanged(progress, onlyOpenNow, type)) {
            currentState = NearbyPlacesVM(
                places = listOf(),
                isLoading = true,
                filters = FiltersVM(
                    radiusProgress = progress,
                    onlyOpenNow = onlyOpenNow,
                    type = type
                )
            )
            fetchInitialData()
        }

        ShowFiltersButton.sendScoped()
    }

    private fun filtersChanged(progress: Int, onlyOpenNow: Boolean, type: String) =
        currentState.filters.let {
            it.radiusProgress != progress ||
            it.onlyOpenNow != onlyOpenNow ||
            it.type != type }


    private fun showFilters() {
        HideFiltersButton.sendScoped()
        val radius = mapProgressToRadius(currentState.filters.radiusProgress)
        UpdateRadiusLabel(radius).sendScoped()
        ShowFilters.sendScoped()
    }

    private fun sendUpdateRadiusLabel(progress: Int) {
        val mappedRadius = mapProgressToRadius(progress)
        UpdateRadiusLabel(mappedRadius).sendScoped()
    }

    private fun mapProgressToRadius(progress: Int): Int {
        val maxRadius = 50000
        return (maxRadius * ((progress.toFloat() + 1) / 100.0f)).toInt()
    }

    private fun showPermissionsDeniedError() {
        ShowError("Permissions must be accepted.").sendScoped()
    }

    private fun loadNextPage() {
        if (!currentState.isLoading && hasMoreResults) {
            currentState = currentState.copy(isLoading = true)

            viewModelScope.launch {
                placesService.getNextPageNearbyPlaces()
                    .fold({ listPlaces ->
                        hasMoreResults = listPlaces.size == 20

                        currentState = with(currentState) {
                            copy(
                                isLoading = false,
                                places = places + listPlaces.map { it.toVM() }
                            )}
                    }, { handleError(it) })
            }
        }
    }

    private fun handleError(error: Exception) {
        currentState = currentState.copy(isLoading = false)
        // If there's no message, it makes no sense to show an empty error
        error.message?.let { message -> ShowError(message).sendScoped() }
        // TODO: Send error to Error tracking service
    }

    private fun handleClickedPlace(position: Int) {
        val place = currentState.places[position]
        ShowDetails(place.id).sendScoped()
    }

    private fun loadInitialData() {
        currentState = currentState.copy(places = listOf(), isLoading = true)
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            val radius = mapProgressToRadius(currentState.filters.radiusProgress)
            run {
                val typeResult = stringToPlaceType(currentState.filters.type)

                typeResult.component1()
                    ?.let {type ->
                        if (currentState.filters.onlyOpenNow) {
                            placesService.getOpenedNearbyPlaces(radius, type)
                        } else {
                            placesService.getNearbyPlaces(radius, type)
                        }
                    } ?: run {
                        typeResult as Result.Failure
                    }
            }.fold({ listPlaces ->
                    hasMoreResults = listPlaces.size == 20
                    currentState = currentState.copy(
                        isLoading = false,
                        places = listPlaces.map { it.toVM() })
                }, { handleError(it) })
        }
    }

    private fun stringToPlaceType(type: String) =
        Result.of<PlaceType, Exception> {
            val locale = Locale.getDefault()
            val upperCaseType = type
                .toUpperCase(locale)
                .replace(" ", "_")
            PlaceType.valueOf(upperCaseType)
        }
}