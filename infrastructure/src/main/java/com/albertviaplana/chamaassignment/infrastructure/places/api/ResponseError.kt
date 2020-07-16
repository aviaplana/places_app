package com.albertviaplana.chamaassignment.infrastructure.places.api

import com.albertviaplana.chamaasignment.AccessDenied
import com.albertviaplana.chamaasignment.ApiException
import com.albertviaplana.chamaasignment.ExceptionUnknown
import java.net.SocketException

sealed class ResponseError: Exception()
object UnknownError: ResponseError()
object OverQueryLimit: ResponseError()
object RequestDenied: ResponseError()
object InvalidRequest: ResponseError()

fun Exception.toDomain() =
    when (this) {
        is OverQueryLimit,
        is InvalidRequest -> ApiException(this)
        is RequestDenied -> AccessDenied(this)
        is SocketException -> ApiException(this)
        else -> ExceptionUnknown(this)
    }
