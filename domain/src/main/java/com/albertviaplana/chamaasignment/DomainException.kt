package com.albertviaplana.chamaasignment

sealed class DomainException(override val message: String? = ""): Exception()
data class AccessDenied(val rootException: Exception? = null): DomainException(rootException?.message)
data class ApiException(val rootException: Exception? = null): DomainException(rootException?.message)
data class ApiUnavailable(val rootException: Exception? = null): DomainException(rootException?.message)
object NetworkException: DomainException("Network error; no connection.")
data class LocationException(override val message: String): DomainException()
data class DeniedPermissionsException(override val message: String): DomainException()
data class ExceptionUnknown(val rootException: Exception? = null): DomainException(rootException?.message)