package com.albertviaplana.chamaassignment.presentation.common

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.databinding.RatingStarsBinding.bind
import com.albertviaplana.chamaassignment.presentation.databinding.RatingStarsBinding

class RatingStarsView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private val binding: RatingStarsBinding
    var rating: Float = .0f
        set(newRating) {
            field = newRating
            val stars: List<ImageView> = listOf(
                binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5
            )
            setStars(stars, newRating)
        }

    init {
        val view = inflate(context, R.layout.rating_stars, this)
        binding = bind(view)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RatingStarsView)
        val placeRating = attributes.getFloat(R.styleable.RatingStarsView_rating, 0.0f)

        rating = placeRating
        attributes.recycle()
    }

    private fun setStars(stars: List<ImageView>, rating: Float) {
        if (rating <= 0.0f) {
            binding.root.visibility = GONE
        } else {
            binding.root.visibility = VISIBLE
            (stars.indices).forEach {
                stars[it].setStar(rating - it)
            }
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