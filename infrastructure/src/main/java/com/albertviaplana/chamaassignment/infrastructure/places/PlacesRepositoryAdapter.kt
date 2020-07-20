package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.DomainException
import com.albertviaplana.chamaasignment.PlacesRepositoryPort
import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.albertviaplana.chamaasignment.entities.PlaceType
import com.albertviaplana.chamaassignment.infrastructure.places.api.toDomain
import com.albertviaplana.chamaassignment.infrastructure.places.entities.toDomain
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import com.github.kittinunf.result.mapError
import java.util.*

class PlacesRepositoryAdapter(private val repository: PlacesRepository): PlacesRepositoryPort {
    override suspend fun getNearbyPlaces(
                location: Coordinates,
                radius: Int,
                type: PlaceType?):
            Result<List<Place>, DomainException> =
        repository.getNearbyPlaces(location, radius,
            type?.toString()?.toLowerCase(Locale.ROOT) ?: "")
                .map { places ->
                    places.map { it.toDomain() }
                }.mapError { it.toDomain() }

    override suspend fun getOpenedNearbyPlaces(
                location: Coordinates,
                radius: Int,
                type: PlaceType?):
            Result<List<Place>, DomainException> =
        repository.getOpenedNearbyPlaces(location, radius,
            type?.toString()?.toLowerCase(Locale.ROOT) ?: "")
                .map { places ->
                    places.map { it.toDomain() }
                }.mapError { it.toDomain() }

    override suspend fun getNextPageNearbyPlaces(): Result<List<Place>, DomainException> =
        repository.getNextPageNearbyPlaces()
            .map { places ->
                places.map { it.toDomain() }
            }.mapError { it.toDomain() }

    override suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails, DomainException> =
        repository.getPlaceDetails(placeId)
            .map { it.toDomain() }
            .mapError { it.toDomain() }
}