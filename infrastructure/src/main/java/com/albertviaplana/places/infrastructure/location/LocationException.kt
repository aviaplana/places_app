package com.albertviaplana.places.infrastructure.location

import com.albertviaplana.places.DeniedPermissionsException
import com.albertviaplana.places.ExceptionUnknown
import java.lang.Exception
import com.albertviaplana.places.LocationException as LocationExceptionDomain

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