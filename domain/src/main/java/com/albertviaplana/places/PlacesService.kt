package com.albertviaplana.places

import com.albertviaplana.places.entities.PlaceType
import com.github.kittinunf.result.flatMap

class PlacesService(
    private val placesRepositoryPort: PlacesRepositoryPort,
    private val locationServicePort: LocationServicePort
) {
    suspend fun getNearbyPlaces(radius: Int, type: PlaceType? = null) =
        locationServicePort.getCurrentLocation()
            .flatMap { placesRepositoryPort.getNearbyPlaces(it, radius, type) }

    suspend fun getOpenedNearbyPlaces(radius: Int, type: PlaceType? = null) =
        locationServicePort.getCurrentLocation()
            .flatMap { placesRepositoryPort.getOpenedNearbyPlaces(it, radius, type) }

    suspend fun getNextPageNearbyPlaces() = placesRepositoryPort.getNextPageNearbyPlaces()

    suspend fun getPlaceDetails(placeId: String) =
        placesRepositoryPort.getPlaceDetails(placeId)
}