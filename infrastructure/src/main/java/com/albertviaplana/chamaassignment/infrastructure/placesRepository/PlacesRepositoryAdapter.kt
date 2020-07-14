package com.albertviaplana.chamaassignment.infrastructure.placesRepository

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.ExceptionUnknown
import com.albertviaplana.chamaasignment.PlacesRepositoryPort
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.ResponseError
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.toDomain
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.entities.toDomain
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import com.github.kittinunf.result.mapError

class PlacesRepositoryAdapter(private val repository: PlacesRepository): PlacesRepositoryPort {
    override suspend fun getNearbyPlaces(location: Coordinates, range: Int): Result<List<Place>, DomainException> =
        repository.getNearbyPlaces(location, range)
            .mapError { mapException(it) }
            .map { prices ->
                prices.map { it.toDomain() }
            }

    override suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails, DomainException> {
        TODO("Not yet implemented")
    }

    private fun mapException(exception: Exception) =
        if (exception is ResponseError) {
            exception.toDomain()
        } else {
            ExceptionUnknown()
    }
}