package com.albertviaplana.places.infrastructure.places.entities

import com.albertviaplana.places.entities.PlaceType
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
