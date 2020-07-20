package com.albertviaplana.places.infrastructure.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.albertviaplana.places.DeniedPermissionsException
import com.github.kittinunf.result.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LocationService(private val context: Context, private val fusedLocationProviderClient: FusedLocationProviderClient ) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Result<Location, LocationException> = suspendCoroutine { task ->
        val deniedPermissions = getDeniedPermissions()

        if (deniedPermissions.isEmpty()) {
            val callback = getLocationCallback(task)
            val locationRequest = getLocationRequest(LocationRequest.PRIORITY_HIGH_ACCURACY)

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null)
        } else {
             Result.error(NoPermissionsException(deniedPermissions))
        }
    }

    private fun getLocationRequest(priority: Int, numUpdates: Int = 1, expirationDuration: Long = 1000) =
        LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)
            .setExpirationDuration(1000)

    private fun getLocationCallback(task: Continuation<Result<Location, LocationException>>) =
        object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                fusedLocationProviderClient.removeLocationUpdates(this)
                task.resume(
                    location?.let {
                        Result.success(it.lastLocation)
                    } ?: run {
                        Result.error(NoLocationException)
                    }
                )
            }
        }



    private fun getDeniedPermissions(): List<String> =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        .filter { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED }
}