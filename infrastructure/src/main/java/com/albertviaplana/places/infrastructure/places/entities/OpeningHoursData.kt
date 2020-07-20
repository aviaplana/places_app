package com.albertviaplana.places.infrastructure.places.entities

import com.albertviaplana.places.entities.OpenStatus


data class OpeningHoursData(
    val openNow: Boolean,
    val weekdayText: List<String>?
)

fun OpeningHoursData?.getOpenStatus() =
    this?.let {
        if (it.openNow) OpenStatus.OPEN
        else OpenStatus.CLOSED
    } ?: OpenStatus.UNKNOWN
