package com.albertviaplana.chamaassignment.infrastructure.places.api

import com.albertviaplana.chamaassignment.infrastructure.places.entities.PlaceData
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int
    ): ResponseWrapper<List<PlaceData>>

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("pagetoken") token: String
    ): ResponseWrapper<List<PlaceData>>
}
