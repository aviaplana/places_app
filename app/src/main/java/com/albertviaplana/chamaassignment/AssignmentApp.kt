package com.albertviaplana.chamaassignment

import android.app.Application
import com.albertviaplana.chamaasignment.di.domainModule
import com.albertviaplana.chamaassignment.infrastructure.di.networkModule
import com.albertviaplana.chamaassignment.infrastructure.location.di.locationServiceModule
import com.albertviaplana.chamaassignment.infrastructure.places.di.placesApiModule
import com.albertviaplana.chamaassignment.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AssignmentApp : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@AssignmentApp)
            modules(
                networkModule,
                placesApiModule,
                locationServiceModule,
                presentationModule,
                domainModule
            )
        }
    }
}