package com.albertviaplana.chamaasignment

sealed class Error: Exception()
data class UnknownError(val exception: java.lang.Exception): Error()