package com.albertviaplana.chamaasignment

import com.github.kittinunf.result.flatMap

class PlacesService(
        private val placesRepository: PlacesRepository,
        private val locationService: LocationService
) {
    suspend fun getNearbyPlaces() =
        locationService.getCurrentLocation()
            .flatMap { placesRepository.getNearbyPlaces(it) }

    suspend fun getPlaceDetails(placeId: String) =
        placesRepository.getPlaceDetails(placeId)
}