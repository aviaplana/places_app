package com.albertviaplana.chamaasignment

import com.github.kittinunf.result.flatMap

class PlacesService(
    private val placesRepositoryPort: PlacesRepositoryPort,
    private val locationServicePort: LocationServicePort
) {
    suspend fun getNearbyPlaces(range: Int) =
        locationServicePort.getCurrentLocation()
            .flatMap { placesRepositoryPort.getNearbyPlaces(it, range) }

    suspend fun getNextPageNearbyPlaces() = placesRepositoryPort.getNextPageNearbyPlaces()

    suspend fun getPlaceDetails(placeId: String) =
        placesRepositoryPort.getPlaceDetails(placeId)
}