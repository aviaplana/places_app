package com.albertviaplana.chamaassignment.infrastructure.placesRepository.api

import com.github.kittinunf.result.Result
import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    val status: ResponseStatus,
    val result: T,
    @SerializedName("next_page_token") val nextPageToken: String?
)

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

fun<T> ResponseWrapper<T>.unwrap() =
    if (status.isSuccess()) {
        Result.success(result)
    } else {
        val exception = status.toException() ?: UnknownError
        Result.error(exception)
    }
