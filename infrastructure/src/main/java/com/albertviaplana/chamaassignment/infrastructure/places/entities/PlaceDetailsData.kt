package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.*
import com.google.gson.annotations.SerializedName

data class PlaceDetailsData(
    @SerializedName("place_id") val id: String,
    val name: String,
    val vicinity: String,
    val icon: String,
    val rating: Float,
    val formattedAddress: String?,
    val formattedPhoneNumber: String?,
    val internationalPhoneNumber: String?,
    val website: String?,
    val url: String,
    val geometry: GeometryData,
    val openingHours: OpeningHoursData?,
    val status: BusinessStatus?,
    val priceLevel: Int,
    val photos: List<PhotoData>?,
    val types: List<String>,
    val reviews: List<ReviewData>?
)

fun PlaceDetailsData.toDomain() =
    PlaceDetails(
        id = id,
        name = name,
        address = formattedAddress.orEmpty(),
        phoneNumber = formattedPhoneNumber.orEmpty(),
        internationalPhoneNumber = internationalPhoneNumber.orEmpty(),
        openHours = openingHours?.weekdayText.orEmpty(),
        website = website.orEmpty(),
        mapsUrl = url,
        vicinity = vicinity,
        iconUrl = icon,
        rating = rating,
        location = geometry.location.toDomain(),
        openStatus = openingHours.getOpenStatus(),
        status = status,
        priceLevel = PriceLevel.getByValue(priceLevel),
        photos = photos.orEmpty().map { it.toDomain() },
        types = types.mapNotNull { mapPlaceType(it) },
        reviews = reviews.orEmpty().map { it.toDomain() }
    )

/*
address_components" : [


      "types" : [ "point_of_interest", "establishment" ],
      "url" : "https://maps.google.com/?cid=10281119596374313554",
      "utc_offset" : 600,
      "vicinity" : "5, 48 Pirrama Road, Pyrmont",
      "website" : "https://www.google.com.au/about/careers/locations/sydney/"

 */