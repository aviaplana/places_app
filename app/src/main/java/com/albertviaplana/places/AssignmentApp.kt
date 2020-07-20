package com.albertviaplana.places

import android.app.Application
import com.albertviaplana.places.di.domainModule
import com.albertviaplana.places.infrastructure.di.networkModule
import com.albertviaplana.places.infrastructure.location.di.locationServiceModule
import com.albertviaplana.places.infrastructure.places.di.placesApiModule
import com.albertviaplana.places.presentation.di.presentationModule
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