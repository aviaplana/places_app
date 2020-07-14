package com.albertviaplana.chamaasignment

sealed class DomainException: Exception()
data class AccessDenied(val originalException: Exception? = null): DomainException()
data class ApiException(val originalException: Exception? = null): DomainException()
data class ApiUnavailable(val originalException: Exception? = null): DomainException()
data class ExceptionUnknown(val originalException: Exception? = null): DomainException()