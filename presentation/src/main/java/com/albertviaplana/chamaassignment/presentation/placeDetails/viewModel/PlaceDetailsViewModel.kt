package com.albertviaplana.chamaassignment.presentation.placeDetails.viewModel

import androidx.lifecycle.viewModelScope
import com.albertviaplana.chamaasignment.PlacesService
import com.albertviaplana.chamaassignment.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PlaceDetailsViewModel(private val placesService: PlacesService):
        BaseViewModel<PlaceDetailsState, DetailsEvent>() {
    override val _state: MutableStateFlow<PlaceDetailsState> =
        MutableStateFlow(Loading)


    infix fun reduce(action: DetailsAction) {
        when(action) {
            is LoadData -> loadData(action.id)
        }
    }

    private fun loadData(id: String?) {
        if (id.isNullOrEmpty()) {
            sendEventViewModelScope(ShowError("No ID!"))
        } else {
            viewModelScope.launch {
                placesService.getPlaceDetails(id)
                    .fold({ details ->
                        currentState = DataLoaded(details.toVM())
                    }, {
                        sendEventViewModelScope(ShowError(it.message.orEmpty()))
                    })
            }
        }
    }
}