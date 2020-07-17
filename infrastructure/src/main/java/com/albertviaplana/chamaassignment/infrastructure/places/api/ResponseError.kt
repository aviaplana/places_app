package com.albertviaplana.chamaassignment.infrastructure.places.api

import com.albertviaplana.chamaasignment.*
import okio.IOException
import java.net.SocketException
import java.net.UnknownHostException

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
        is IOException -> NetworkException
        is SocketException -> ApiException(this)
        else -> ExceptionUnknown(this)
    }
