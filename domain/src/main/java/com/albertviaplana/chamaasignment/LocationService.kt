package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates
import com.github.kittinunf.result.Result

interface LocationService {
    suspend fun getCurrentLocation(): Result<Coordinates, Error>
}