package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaassignment.infrastructure.places.api.PlacesApi
import com.albertviaplana.chamaassignment.infrastructure.places.api.unwrap
import com.albertviaplana.chamaassignment.infrastructure.places.entities.PlaceDetailsData
import com.github.kittinunf.result.Result

class PlacesRepository(private val api: PlacesApi) {
    var nextPageToken: String? = null

    //TODO: Implement runcatching equivalent
    suspend fun getNearbyPlaces(coordinates: Coordinates, radius: Int, type: String) =
        if (radius > MAX_RADIUS) {
            Result.error(Exception("Radius exceeds maximum ($MAX_RADIUS)"))
        } else {
            try {
                val (token, result) =
                    api.getNearbyPlaces(coordinates.toString(), radius, type)
                        .unwrap()
                nextPageToken = token
                Result.success(result)
            } catch (e: Exception) {
                Result.error(e)
            }
        }
    suspend fun getOpenedNearbyPlaces(coordinates: Coordinates, radius: Int, type: String) =
        if (radius > MAX_RADIUS) {
            Result.error(Exception("Radius exceeds maximum ($MAX_RADIUS)"))
        } else {
            try {
                val (token, result) =
                    api.getOpenedNearbyPlaces(coordinates.toString(), radius, type)
                        .unwrap()
                nextPageToken = token
                Result.success(result)
            } catch (e: Exception) {
                Result.error(e)
            }
        }

    suspend fun getNextPageNearbyPlaces() =
        nextPageToken?.let {
            try {
                val (token, result) = api.getNearbyPlaces(it)
                    .unwrap()
                nextPageToken = token
                Result.success(result)
            } catch (e: Exception) {
                Result.error(e)
            }
        } ?: Result.success(listOf()) // No more pages te fetch

    suspend fun getPlaceDetails(id: String): Result<PlaceDetailsData, Exception> =
        try {
            Result.success(api.getPlaceDetails(id)
                .unwrap())
        } catch (e: Exception) {
            Result.error(e)
        }

    companion object {
        const val MAX_RADIUS = 50_000
    }
}