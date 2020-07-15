package com.albertviaplana.chamaassignment.infrastructure.placesRepository.di

import com.albertviaplana.chamaassignment.infrastructure.BuildConfig
import com.albertviaplana.chamaassignment.infrastructure.di.provideOkHttpClient
import com.albertviaplana.chamaassignment.infrastructure.di.provideRetrofit
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.PlacesAuthInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val placesApiModule = module {
    factory { PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY) }

    factory(named("OkHttpWithPlacesAuth")) {
        provideOkHttpClient(
            get(named("PlacesAuthInterceptor"))
        )
    }

    single(named("PlacesRetrofit")) {
        provideRetrofit(
            get(named("OkHttpWithPlacesAuth")),
            get(),
            BuildConfig.PLACES_API_URL)
    }
}
