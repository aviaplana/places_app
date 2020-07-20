package com.albertviaplana.places.infrastructure.places.di

import com.albertviaplana.places.PlacesRepositoryPort
import com.albertviaplana.places.infrastructure.BuildConfig
import com.albertviaplana.places.infrastructure.di.provideOkHttpClient
import com.albertviaplana.places.infrastructure.di.provideRetrofit
import com.albertviaplana.places.infrastructure.places.PlacesRepository
import com.albertviaplana.places.infrastructure.places.PlacesRepositoryAdapter
import com.albertviaplana.places.infrastructure.places.api.PlacesApi
import com.albertviaplana.places.infrastructure.places.api.PlacesAuthInterceptor
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

    factory { PlacesRepositoryAdapter(get()) as PlacesRepositoryPort }
}

fun provideOkHttpWithPlacesAuth(): OkHttpClient {
    val authInterceptor = PlacesAuthInterceptor(BuildConfig.PLACES_API_KEY)
    return provideOkHttpClient(authInterceptor)
}

fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(
    PlacesApi::class.java)
