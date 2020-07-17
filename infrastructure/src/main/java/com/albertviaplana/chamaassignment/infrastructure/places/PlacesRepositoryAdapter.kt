package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.ExceptionUnknown
import com.albertviaplana.chamaasignment.PlacesRepositoryPort
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.albertviaplana.chamaassignment.infrastructure.places.api.ResponseError
import com.albertviaplana.chamaassignment.infrastructure.places.api.toDomain
import com.albertviaplana.chamaassignment.infrastructure.places.entities.toDomain
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import com.github.kittinunf.result.mapError

class PlacesRepositoryAdapter(private val repository: PlacesRepository): PlacesRepositoryPort {
    override suspend fun getNearbyPlaces(location: Coordinates, range: Int):
            Result<List<Place>, DomainException> =
        repository.getNearbyPlaces(location, range)
            .map { prices ->
                prices.map { it.toDomain() }
            }.mapError { it.toDomain() }

    override suspend fun getNextPageNearbyPlaces(): Result<List<Place>, DomainException> =
        repository.getNextPageNearbyPlaces()
            .map { prices ->
                prices.map { it.toDomain() }
            }.mapError { it.toDomain() }

    override suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails, DomainException> {
        TODO("Not yet implemented")
    }
}