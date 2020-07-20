package com.albertviaplana.places.presentation.placeDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.albertviaplana.places.presentation.R
import com.albertviaplana.places.presentation.databinding.PhotoCellBinding

class PhotosAdapter: RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
    private val photos: MutableList<String> = mutableListOf()

    class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = PhotoCellBinding.bind(view)

        fun bind(src: String) {
            binding.photo.load(src)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val photoView = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_cell, parent, false)

        return PhotoViewHolder(photoView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size

    fun setPhotos(items: List<String>) {
        photos.clear()
        photos.addAll(items)
        notifyDataSetChanged()
    }
}