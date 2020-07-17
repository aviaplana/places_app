package com.albertviaplana.chamaassignment.infrastructure.location

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.LocationServicePort
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.github.kittinunf.result.Result

class LocationServiceAdapter: LocationServicePort {
    override suspend fun getCurrentLocation(): Result<Coordinates, DomainException> =
        Result.success(Coordinates(42.3675294, -71.186966))
}