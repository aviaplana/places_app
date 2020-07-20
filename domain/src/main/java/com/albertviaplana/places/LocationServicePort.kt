package com.albertviaplana.places

import com.albertviaplana.places.entities.Coordinates
import com.github.kittinunf.result.Result

interface LocationServicePort {
    suspend fun getCurrentLocation(): Result<Coordinates, DomainException>
}