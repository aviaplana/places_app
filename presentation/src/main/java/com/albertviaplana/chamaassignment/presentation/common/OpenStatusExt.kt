package com.albertviaplana.chamaassignment.presentation.common

import android.content.Context
import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaassignment.presentation.R

fun OpenStatus.getIsOpenText(context: Context) =
    if (this == OpenStatus.UNKNOWN) {
        ""
    } else {
        val stringId =
            if (this == OpenStatus.OPEN) R.string.open_now
            else R.string.closed
        context.getString(stringId)
    }