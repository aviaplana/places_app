package com.albertviaplana.chamaassignment.infrastructure.location

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.LocationServicePort
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map

class LocationServiceAdapter(private val locationService: LocationService): LocationServicePort {
    override suspend fun getCurrentLocation(): Result<Coordinates, DomainException> =
        locationService.getLocation()
            .map { Coordinates(it.latitude, it.longitude) }
}