package com.albertviaplana.chamaassignment.infrastructure.places.di

import com.albertviaplana.chamaasignment.PlacesRepositoryPort
import com.albertviaplana.chamaassignment.infrastructure.BuildConfig
import com.albertviaplana.chamaassignment.infrastructure.di.provideOkHttpClient
import com.albertviaplana.chamaassignment.infrastructure.di.provideRetrofit
import com.albertviaplana.chamaassignment.infrastructure.places.PlacesRepository
import com.albertviaplana.chamaassignment.infrastructure.places.PlacesRepositoryAdapter
import com.albertviaplana.chamaassignment.infrastructure.places.api.PlacesApi
import com.albertviaplana.chamaassignment.infrastructure.places.api.PlacesAuthInterceptor
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val placesApiModule = module {
    single(named("PlacesAuthInterceptor")) { PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY) }

    single(named("OkHttpWithPlacesAuth")) { provideOkHttpWithPlacesAuth() }

    single(named("PlacesRetrofit")) {
        provideRetrofit(
            get(named("OkHttpWithPlacesAuth")),
            get(),
            BuildConfig.PLACES_API_URL.toHttpUrl()
        )
    }

    single { providePlacesApi(get(named("PlacesRetrofit"))) }

    factory { PlacesRepository(get()) }

    single { PlacesRepositoryAdapter(get()) as PlacesRepositoryPort }
}

fun provideOkHttpWithPlacesAuth(): OkHttpClient {
    val authInterceptor = PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY)
    return provideOkHttpClient(authInterceptor)
}

fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(
    PlacesApi::class.java)
