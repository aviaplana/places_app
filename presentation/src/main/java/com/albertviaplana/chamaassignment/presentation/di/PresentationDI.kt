package com.albertviaplana.chamaassignment.presentation.di

import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.NearbyPlacesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { NearbyPlacesViewModel(get()) }
}