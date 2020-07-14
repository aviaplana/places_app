package com.albertviaplana.chamaasignment

import com.albertviaplana.chamaasignment.entities.Coordinates

interface LocationService {
    suspend fun getCurrentLocation(): Coordinates
}