package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails

interface PlacesRepository {
    suspend fun getNearbyPlaces(location: Coordinates): List<Place>
    suspend fun getPlaceDetails(placeId: String): PlaceDetails
}