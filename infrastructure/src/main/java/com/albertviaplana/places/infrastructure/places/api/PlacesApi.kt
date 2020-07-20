package com.albertviaplana.places.infrastructure.places.api

import com.albertviaplana.places.infrastructure.places.entities.PlaceData
import com.albertviaplana.places.infrastructure.places.entities.PlaceDetailsData
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") types: String
    ): NearbyPlacesReponseWrapper<List<PlaceData>>

    @GET("place/nearbysearch/json?opennow")
    suspend fun getOpenedNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") types: String
    ): NearbyPlacesReponseWrapper<List<PlaceData>>

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("pagetoken") token: String
    ): NearbyPlacesReponseWrapper<List<PlaceData>>

    @GET("place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") id: String
    ): PlaceDetailsResponseWrapper<PlaceDetailsData>
}
