package com.albertviaplana.places.presentation.nearbyPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.albertviaplana.places.entities.OpenStatus
import com.albertviaplana.places.presentation.R
import com.albertviaplana.places.presentation.common.getIsOpenText
import com.albertviaplana.places.presentation.databinding.PlaceCardBinding
import com.albertviaplana.places.presentation.nearbyPlaces.viewModel.PlaceVM

class PlacesAdapter(val onClickAction: ((position: Int) -> Unit)? = null):
        RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {
    private val places: MutableList<PlaceVM> = mutableListOf()

    class PlaceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = PlaceCardBinding.bind(view)

        fun bind(place: PlaceVM) {
            with(binding) {
                placeTitle.text = place.name
                placeIsOpen.text = place.openStatus.getIsOpenText(binding.root.context)
                placeVicinity.text = place.vicinity
                placeRating.rating = place.rating
                placeIcon.load(place.iconUrl)
            }
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