package com.albertviaplana.chamaassignment.infrastructure.location

import com.albertviaplana.chamaasignment.DeniedPermissionsException
import com.albertviaplana.chamaasignment.ExceptionUnknown
import java.lang.Exception
import com.albertviaplana.chamaasignment.LocationException as LocationExceptionDomain

sealed class LocationException(override val message: String = ""): Exception()
object NoLocationException: LocationException("Unable to get current location")
data class NoPermissionsException(val permissions: List<String>):
    LocationException("Permissions ${permissions.joinToString()} denied")

fun LocationException.toDomain() =
    when(this) {
        is NoLocationException -> LocationExceptionDomain(this.message)
        is NoPermissionsException -> DeniedPermissionsException(this.message)
        else -> ExceptionUnknown(this)
    }