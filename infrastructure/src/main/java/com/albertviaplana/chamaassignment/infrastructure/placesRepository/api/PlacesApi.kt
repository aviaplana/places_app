package com.albertviaplana.chamaassignment.infrastructure.placesRepository.api

import com.albertviaplana.chamaassignment.infrastructure.placesRepository.entities.PlaceData
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("place/nearbysearch/")
    suspend fun getTopHeadlines(
        @Query("location") location: String,
        @Query("radius") radius: Int
    ): ResponseWrapper<List<PlaceData>>
}
