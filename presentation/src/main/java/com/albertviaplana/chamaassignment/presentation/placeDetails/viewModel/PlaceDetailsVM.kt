package com.albertviaplana.chamaassignment.presentation.placeDetails.viewModel

import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaasignment.entities.PlaceDetails
import com.albertviaplana.chamaasignment.entities.Review

sealed class PlaceDetailsState
object Loading: PlaceDetailsState()
data class DataLoaded(val data: DetailsVM): PlaceDetailsState()

data class DetailsVM(
    val name: String,
    val address: String,
    val phoneNumber: String,
    val rating: Float,
    val priceLevel: String,
    val mapsUrl: String,
    val webSite: String,
    val openStatus: OpenStatus,
    val openHours: List<String>,
    val photos: List<String>,
    val reviews: List<ReviewVM>
)

data class ReviewVM(
    val name: String,
    val profilePhotoUrl: String,
    val rating: Float,
    val text: String,
    val date: String
)

fun PlaceDetails.toVM() =
    DetailsVM(
        name = name,
        address = address,
        phoneNumber = phoneNumber,
        priceLevel = priceLevel.name,
        mapsUrl = mapsUrl,
        webSite = website,
        rating = rating,
        openStatus = openStatus,
        openHours = openHours,
        photos = photos.map { it.url },
        reviews = reviews.map{ it.toVM() }
    )

fun Review.toVM() = ReviewVM(
    name = authorName,
    profilePhotoUrl = profilePhotoUrl,
    rating = rating,
    text = text,
    date = relativeDateDescription
)

sealed class DetailsAction
data class LoadData(val id: String?): DetailsAction()

sealed class DetailsEvent
data class ShowError(val message: String): DetailsEvent()

