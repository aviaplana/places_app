package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.PlaceType
import java.lang.Exception
import java.util.*


fun mapPlaceType(type: String) =
    try {
        PlaceType.valueOf(
            type.toUpperCase(Locale.ROOT)
        )
    } catch(e: Exception) {
        null
    }
