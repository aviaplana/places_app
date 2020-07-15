package com.albertviaplana.chamaassignment.infrastructure.placesRepository.di

import com.albertviaplana.chamaassignment.infrastructure.BuildConfig
import com.albertviaplana.chamaassignment.infrastructure.di.provideOkHttpClient
import com.albertviaplana.chamaassignment.infrastructure.di.provideRetrofit
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.PlacesApi
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.PlacesAuthInterceptor
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val placesApiModule = module {
    factory { PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY) }

    factory(named("OkHttpWithPlacesAuth")) {
        provideOkHttpClient(
            get(named("PlacesAuthInterceptor"))
        )
    }

    factory {
        providePlacesApi(get(named("PlacesRetrofit")))
    }

    single(named("PlacesRetrofit")) {
        provideRetrofit(
            get(named("OkHttpWithPlacesAuth")),
            get(),
            BuildConfig.PLACES_API_URL.toHttpUrl()
        )
    }
}

fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(
    PlacesApi::class.java)
