package com.albertviaplana.chamaassignment

import android.app.Application
import com.albertviaplana.chamaassignment.infrastructure.di.networkModule
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.di.placesApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AssignmentApp : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@AssignmentApp)
            modules(networkModule, placesApiModule)
        }
    }
}