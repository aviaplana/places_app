package com.albertviaplana.places.infrastructure.location.di

import android.content.Context
import com.albertviaplana.places.LocationServicePort
import com.albertviaplana.places.infrastructure.location.LocationService
import com.albertviaplana.places.infrastructure.location.LocationServiceAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module


val locationServiceModule = module {
    single { LocationServiceAdapter(LocationService(get(), get())) as LocationServicePort}
    factory { provideFusedLocationProviderClient(get()) }
}

fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)