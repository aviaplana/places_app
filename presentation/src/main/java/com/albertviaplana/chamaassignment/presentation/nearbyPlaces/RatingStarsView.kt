package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.albertviaplana.chamaassignment.presentation.R

class RatingStarsView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.rating_stars, this)
        val stars: List<ImageView> = listOf(findViewById(R.id.star_1),
                findViewById(R.id.star_2),
                findViewById(R.id.star_3),
                findViewById(R.id.star_4),
                findViewById(R.id.star_5))
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RatingStarsView)
        val rating = attributes.getFloat(R.styleable.RatingStarsView_rating, 0.0f)

        setStars(stars, rating)
        attributes.recycle()
    }

    private fun setStars(stars: List<ImageView>, rating: Float) {
        (0..stars.size).forEach {
            stars[it].setStar(rating - it)
        }
    }

    private fun ImageView.setStar(rating: Float) {
        val drawableId = when {
                rating >= 1 -> R.drawable.ic_star_24px
                rating > 0 -> R.drawable.ic_star_half_24px
                else -> R.drawable.ic_star_border_24px
            }
        val drawable = context.getDrawable(drawableId)
        setImageDrawable(drawable)
    }
}