package com.albertviaplana.chamaassignment.infrastructure.placesRepository.api

import com.albertviaplana.chamaasignment.AccessDenied
import com.albertviaplana.chamaasignment.ApiException
import com.albertviaplana.chamaasignment.ExceptionUnknown

sealed class ResponseError: Exception()
object UnknownError: ResponseError()
object OverQueryLimit: ResponseError()
object RequestDenied: ResponseError()
object InvalidRequest: ResponseError()

fun ResponseError.toDomain() =
    when (this) {
        is UnknownError -> ExceptionUnknown(this)
        is OverQueryLimit,
        is InvalidRequest -> ApiException(this)
        is RequestDenied -> AccessDenied(this)
    }
