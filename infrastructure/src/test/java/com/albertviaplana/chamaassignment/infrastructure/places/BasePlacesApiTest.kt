package com.albertviaplana.chamaassignment.infrastructure.places

import com.albertviaplana.chamaasignment.entities.BusinessStatus
import com.albertviaplana.chamaasignment.entities.PriceLevel
import com.albertviaplana.chamaassignment.infrastructure.places.entities.*

abstract class BasePlacesApiTest {
    protected fun getMockPlaceData(): PlaceData {
        val photoData = PhotoData(
            width = 10,
            height = 20,
            reference = "Photo Reference"
        )

        return PlaceData(
            id = "Place id",
            name = "Name",
            vicinity = "Vicinity",
            icon = "Icon url",
            rating = 3.5f,
            geometry = GeometryData(LocationData(1.toDouble(), 2.toDouble())),
            openingHours = OpeningHoursData(true, null),
            status = BusinessStatus.OPERATIONAL,
            priceLevel = PriceLevel.MODERATE.level,
            photos = listOf(photoData),
            types = listOf("BAR", "CAFE")
        )
    }
}