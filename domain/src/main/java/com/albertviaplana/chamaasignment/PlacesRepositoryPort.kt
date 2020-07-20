package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.albertviaplana.chamaasignment.entities.PlaceType
import com.github.kittinunf.result.Result

interface PlacesRepositoryPort {
    suspend fun getNearbyPlaces(
            location: Coordinates,
            radius: Int = 1_000,
            type: PlaceType? = null):
        Result<List<Place>, DomainException>
    suspend fun getOpenedNearbyPlaces(
            location: Coordinates,
            radius: Int = 1_000,
            type: PlaceType? = null):
        Result<List<Place>, DomainException>
    suspend fun getNextPageNearbyPlaces(): Result<List<Place>, DomainException>
    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails, DomainException>
}