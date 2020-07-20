package com.albertviaplana.places.presentation.di

import com.albertviaplana.places.presentation.nearbyPlaces.viewModel.NearbyPlacesViewModel
import com.albertviaplana.places.presentation.placeDetails.viewModel.PlaceDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val presentationModule = module {
    viewModel { NearbyPlacesViewModel(get(), null) }
    viewModel { PlaceDetailsViewModel(get(), null) }
}