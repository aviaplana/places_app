package com.albertviaplana.chamaassignment.infrastructure.places.api

enum class ResponseStatus {
    OK,
    ZERO_RESULTS,
    OVER_QUERY_LIMIT,
    REQUEST_DENIED,
    INVALID_REQUEST,
    UNKNOWN_ERROR
}

fun ResponseStatus.isSuccess() =
    this == ResponseStatus.OK || this == ResponseStatus.ZERO_RESULTS

fun ResponseStatus.toException() =
    when {
        this ==  ResponseStatus.OVER_QUERY_LIMIT -> OverQueryLimit
        this ==  ResponseStatus.REQUEST_DENIED -> RequestDenied
        this ==  ResponseStatus.INVALID_REQUEST -> InvalidRequest
        this ==  ResponseStatus.UNKNOWN_ERROR -> UnknownError
        else -> null
    }
