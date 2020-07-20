package com.albertviaplana.chamaassignment.infrastructure.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.albertviaplana.chamaasignment.DeniedPermissionsException
import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.LocationException
import com.github.kittinunf.result.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LocationService(private val context: Context, private val fusedLocationProviderClient: FusedLocationProviderClient ) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Result<Location, DomainException> = suspendCoroutine { task ->
        val deniedPermissions = getDeniedPermissions()

        if (deniedPermissions.isEmpty()) {
            val callback = object : LocationCallback() {
                override fun onLocationResult(location: LocationResult?) {
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    task.resume(
                            location?.let {
                                Result.success(it.lastLocation)
                            } ?: run {
                                Result.error(LocationException("Unable to get current location"))
                            }
                    )
                }
            }

            val locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1)
                    .setExpirationDuration(1000)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null)
        } else {
             Result.error(DeniedPermissionsException("Permissions ${deniedPermissions.joinToString()} denied"))
        }
    }

    private fun getDeniedPermissions(): List<String> =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        .filter { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED }
}