package com.albertviaplana.chamaassignment.infrastructure.places.entities

import com.albertviaplana.chamaasignment.entities.OpenStatus


data class OpeningHoursData(
    val isOpen: Boolean,
    val weekdayText: List<String>?
)

fun OpeningHoursData?.getOpenStatus() =
    this?.let {
        if (it.isOpen) OpenStatus.OPEN
        else OpenStatus.CLOSED
    } ?: OpenStatus.UNKNOWN
