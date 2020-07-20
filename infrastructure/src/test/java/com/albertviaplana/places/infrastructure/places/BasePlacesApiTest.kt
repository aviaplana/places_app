package com.albertviaplana.places.infrastructure.places

import com.albertviaplana.places.entities.BusinessStatus
import com.albertviaplana.places.entities.PriceLevel
import com.albertviaplana.places.infrastructure.places.entities.*

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