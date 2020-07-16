package com.albertviaplana.chamaasignment.di

import com.albertviaplana.chamaasignment.PlacesService
import org.koin.dsl.module

val domainModule = module {
    factory { PlacesService(get(), get()) }
}