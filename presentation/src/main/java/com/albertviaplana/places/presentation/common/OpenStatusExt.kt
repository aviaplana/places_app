package com.albertviaplana.places.presentation.common

import android.content.Context
import com.albertviaplana.places.entities.OpenStatus
import com.albertviaplana.places.presentation.R

fun OpenStatus.getIsOpenText(context: Context) =
    if (this == OpenStatus.UNKNOWN) {
        ""
    } else {
        val stringId =
            if (this == OpenStatus.OPEN) R.string.open_now
            else R.string.closed
        context.getString(stringId)
    }