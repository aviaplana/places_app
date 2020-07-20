package com.albertviaplana.chamaassignment.infrastructure.location.di

import android.content.Context
import com.albertviaplana.chamaasignment.LocationServicePort
import com.albertviaplana.chamaassignment.infrastructure.location.LocationService
import com.albertviaplana.chamaassignment.infrastructure.location.LocationServiceAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module


val locationServiceModule = module {
    single { LocationServiceAdapter(LocationService(get(), get())) as LocationServicePort}
    factory { provideFusedLocationProviderClient(get()) }
}

fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)