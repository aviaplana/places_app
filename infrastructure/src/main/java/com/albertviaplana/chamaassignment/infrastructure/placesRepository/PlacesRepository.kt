package com.albertviaplana.chamaassignment.infrastructure.placesRepository

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.PlacesApi
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.api.unwrap
import com.albertviaplana.chamaassignment.infrastructure.placesRepository.entities.PlaceData
import com.github.kittinunf.result.Result

class PlacesRepository(private val api: PlacesApi) {
    suspend fun getNearbyPlaces(coordinates: Coordinates, range: Int): Result<List<PlaceData>, Exception> {
        val maxRange = 50_000

        return if (range > maxRange) {
            Result.error(Exception("Range exceeds maximum ($maxRange)"))
        } else {
            api.getTopHeadlines(coordinates.toString(), range)
                .unwrap()
        }
    }
}