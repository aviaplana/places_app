package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.albertviaplana.chamaasignment.entities.OpenStatus
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.databinding.PlaceCardBinding
import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.PlaceVM

class PlacesAdapter(val onClickAction: ((position: Int) -> Unit)? = null):
        RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {
    private val places: MutableList<PlaceVM> = mutableListOf()

    class PlaceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = PlaceCardBinding.bind(view)

        fun bind(place: PlaceVM) {
            with(binding) {
                placeTitle.text = place.name
                placeIsOpen.text = getIsOpenText(place.openStatus)
                placeVicinity.text = place.vicinity
                placeRating.rating = place.rating
            }
        }

        private fun getIsOpenText(openStatus: OpenStatus) =
            if (openStatus == OpenStatus.UNKNOWN) {
                ""
            } else {
                val stringId =
                    if (openStatus == OpenStatus.OPEN) R.string.open_now
                    else R.string.closed
                binding.root.context.getString(stringId)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_card, parent, false)

        return PlaceViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
        onClickAction?.let{ action ->
            holder.itemView.setOnClickListener {
                action(position)
            }
        }
    }

    override fun getItemCount() = places.size

    fun setItems(items: List<PlaceVM>) {
        val currentSize = places.size
        places.clear()
        places.addAll(items)

        if (items.isNotEmpty()) {
            val offset = currentSize - items.size

            if (offset != 0) {
                notifyItemRangeInserted(currentSize, items.size)
            }
        } else {
            notifyItemRangeRemoved(0, currentSize)
        }

    }
}