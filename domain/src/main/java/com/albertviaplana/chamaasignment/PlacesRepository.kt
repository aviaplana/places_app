package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.albertviaplana.chamaasignment.entities.Place
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.github.kittinunf.result.Result

interface PlacesRepository {
    suspend fun getNearbyPlaces(location: Coordinates): Result<List<Place>, Error>
    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails, Error>
}