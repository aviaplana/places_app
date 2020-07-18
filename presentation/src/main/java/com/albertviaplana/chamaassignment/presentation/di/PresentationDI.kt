package com.albertviaplana.chamaassignment.presentation.di

import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.NearbyPlacesViewModel
import com.albertviaplana.chamaassignment.presentation.placeDetails.viewModel.PlaceDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val presentationModule = module {
    viewModel { NearbyPlacesViewModel(get()) }
    viewModel { PlaceDetailsViewModel(get()) }
}