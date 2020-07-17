package com.albertviaplana.chamaassignment.infrastructure.location.di

import com.albertviaplana.chamaasignment.LocationServicePort
import com.albertviaplana.chamaassignment.infrastructure.location.LocationService
import com.albertviaplana.chamaassignment.infrastructure.location.LocationServiceAdapter
import org.koin.dsl.module


val locationServiceModule = module {
    single { LocationServiceAdapter(LocationService(get())) as LocationServicePort}
}

