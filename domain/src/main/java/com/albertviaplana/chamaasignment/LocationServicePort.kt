package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.github.kittinunf.result.Result

interface LocationServicePort {
    suspend fun getCurrentLocation(): Result<Coordinates, DomainException>
}