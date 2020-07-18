package com.albertviaplana.chamaassignment.infrastructure.places.api

data class PlaceDetailsResponseWrapper<T>(
    val status: ResponseStatus,
    val result: T
)

fun<T> PlaceDetailsResponseWrapper<T>.unwrap() =
    if (status.isSuccess()) result
    else throw status.toException() ?: UnknownError

