package com.albertviaplana.chamaassignment.presentation.common

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun BottomSheetDialog.onHide(action: () -> Unit) {
    setOnDismissListener {
        action()
    }

    behavior.addBottomSheetCallback(
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) { action() }
            }

            override fun onSlide(view: View, v: Float) {}
        })
    }
