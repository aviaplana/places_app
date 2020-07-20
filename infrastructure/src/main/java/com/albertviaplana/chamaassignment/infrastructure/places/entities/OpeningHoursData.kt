package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.OpenStatus


data class OpeningHoursData(
    val openNow: Boolean,
    val weekdayText: List<String>?
)

fun OpeningHoursData?.getOpenStatus() =
    this?.let {
        if (it.openNow) OpenStatus.OPEN
        else OpenStatus.CLOSED
    } ?: OpenStatus.UNKNOWN
