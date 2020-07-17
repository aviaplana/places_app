package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import androidx.lifecycle.viewModelScope
import com.albertviaplana.chamaasignment.PlacesService
import com.albertviaplana.chamaassignment.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NearbyPlacesViewModel(private val placesService: PlacesService): BaseViewModel<NearbyPlacesVM>() {
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
        if (!_state.value.isLoading && hasMoreResults) {
            _state.value = _state.value.copy(isLoading = true)

            viewModelScope.launch {
                placesService.getNextPageNearbyPlaces()
                    .fold({ listPlaces ->
                        hasMoreResults = listPlaces.size == 20
                        _state.value = with(_state.value) {
                            copy(
                                isLoading = false,
                                places = places + listPlaces.map { it.toVM() }
                            )}
                        }, {
                            _state.value = _state.value.copy(isLoading = false)
                            println("Exception! $it")
                        }
                    )
            }
        }
    }

    private fun handleClickedPlace(position: Int) {
        val place = _state.value.places[position]
        println("Clicked place ${place.name}, position $position")
    }

    private suspend fun loadInitialData() {
        _state.value = _state.value.copy(isLoading = true)
        placesService.getNearbyPlaces(1000)
            .fold({listPlaces ->
                hasMoreResults = listPlaces.size == 20
                _state.value =_state.value.copy(
                    isLoading = false,
                    places = listPlaces.map { it.toVM() })
                }, {
                    _state.value = _state.value.copy(isLoading = false)
                    println("Exception! $it")
                }
            )
    }
}