package com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel

import androidx.lifecycle.viewModelScope
import com.albertviaplana.chamaasignment.PlacesService
import com.albertviaplana.chamaassignment.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NearbyPlacesViewModel(private val placesService: PlacesService): BaseViewModel<NearbyPlacesVM>() {

    init {
        viewModelScope.launch {
            loadInitialData()
        }
    }

    private suspend fun loadInitialData() {
        placesService.getNearbyPlaces(1000)
            .fold({listPlaces ->
                val placesVM = NearbyPlacesVM(
                    places = listPlaces.map { it.toVM() }
                )
                _state.value = placesVM
            }, {
                println("Exception! $it")
            })
    }
}