package com.albertviaplana.places.di

import com.albertviaplana.places.PlacesService
import org.koin.dsl.module

val domainModule = module {
    factory { PlacesService(get(), get()) }
}