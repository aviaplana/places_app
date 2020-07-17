package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import androidx.lifecycle.viewModelScope
import com.albertviaplana.chamaasignment.PlacesService
import com.albertviaplana.chamaassignment.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NearbyPlacesViewModel(private val placesService: PlacesService): BaseViewModel<NearbyPlacesVM, Event>() {
    override val _state: MutableStateFlow<NearbyPlacesVM> =
        MutableStateFlow(NearbyPlacesVM(
            places = listOf(),
            isLoading = false
        ))

    var hasMoreResults= false

    init {
        viewModelScope.launch {
            loadInitialData()
        }
    }

    infix fun notify(action: Action) {
        when(action) {
            is LoadData -> loadNextPage()
            is ClickedPlace -> handleClickedPlace(action.position)
        }
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
                        }, {
                            currentState = currentState.copy(isLoading = false)
                            sendEventViewModelScope(ShowError(it.message.orEmpty()))
                        }
                    )
            }
        }
    }

    private fun handleClickedPlace(position: Int) {
        val place = currentState.places[position]
        sendEventViewModelScope(ShowDetails)
        println("Clicked place ${place.name}, position $position")
    }

    private suspend fun loadInitialData() {
        currentState = currentState.copy(isLoading = true)
        placesService.getNearbyPlaces(1000)
            .fold({listPlaces ->
                hasMoreResults = listPlaces.size == 20
                currentState = currentState.copy(
                    isLoading = false,
                    places = listPlaces.map { it.toVM() })
                }, {
                    currentState = currentState.copy(isLoading = false)
                    sendEventViewModelScope(ShowError(it.message.orEmpty()))
                }
            )
    }
}