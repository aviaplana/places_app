package com.albertviaplana.places.infrastructure.location

import com.albertviaplana.places.DomainException
import com.albertviaplana.places.LocationServicePort
import com.albertviaplana.places.entities.Coordinates
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import com.github.kittinunf.result.mapError

class LocationServiceAdapter(private val locationService: LocationService): LocationServicePort {
    override suspend fun getCurrentLocation(): Result<Coordinates, DomainException> =
        locationService.getLocation()
            .map { Coordinates(it.latitude, it.longitude) }
            .mapError { it.toDomain() }
}