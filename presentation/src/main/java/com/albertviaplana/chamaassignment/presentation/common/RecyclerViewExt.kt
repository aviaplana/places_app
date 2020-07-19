package com.albertviaplana.chamaassignment.presentation.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onReachEnd(offset: Int = 5, action: () -> Unit): RecyclerView {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            //only downwards scrolling
            if (dy > 0) {
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val numItems = manager.itemCount
                val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()

                if ((offset + lastVisiblePosition) >= numItems) {
                    action()
                }
            }
        }
    })

    return this
}

fun RecyclerView.onScrollDown(action: () -> Unit): RecyclerView {
    addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                action()
            }
        }
    })

    return this
}

fun RecyclerView.onScrollUp(action: () -> Unit): RecyclerView {
    addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy < 0) {
                action()
            }
        }
    })

    return this
}

