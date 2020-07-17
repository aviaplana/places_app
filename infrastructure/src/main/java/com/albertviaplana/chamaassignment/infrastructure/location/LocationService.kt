package com.albertviaplana.chamaassignment.infrastructure.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.albertviaplana.chamaasignment.DeniedPermissionsException
import com.albertviaplana.chamaasignment.DomainException
import com.github.kittinunf.result.Result


class LocationService(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getLocation(): Result<Location, DomainException> {
        val deniedPermissions = getDeniedPermissions()

        return if (deniedPermissions.isEmpty()) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            Result.success(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER))
        } else {
            Result.error(DeniedPermissionsException("Permissions ${deniedPermissions.joinToString() } denied"))
        }
    }

    private fun getDeniedPermissions(): List<String> =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        .filter { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED }
}