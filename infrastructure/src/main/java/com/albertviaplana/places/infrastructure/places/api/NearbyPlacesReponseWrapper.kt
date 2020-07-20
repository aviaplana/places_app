package com.albertviaplana.places.infrastructure.places.api

import com.google.gson.annotations.SerializedName

data class NearbyPlacesReponseWrapper<T>(
    val status: ResponseStatus,
    val results: T,
    @SerializedName("next_page_token") val nextPageToken: String?
)

fun<T> NearbyPlacesReponseWrapper<T>.unwrap() =
    if (status.isSuccess()) {
        Pair(nextPageToken, results)
    } else {
        throw status.toException() ?: UnknownError
    }
