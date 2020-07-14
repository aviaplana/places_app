package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Place

class PlacesService(
        private val placesRepository: PlacesRepository,
        private val locationService: LocationService
) {
    suspend fun getNearbyPlaces(): List<Place> {
        val currentLocation = locationService.getCurrentLocation()
        return placesRepository.getNearbyPlaces(currentLocation)
    }

    suspend fun getPlaceDetails(placeId: String) =
        placesRepository.getPlaceDetails(placeId)
}