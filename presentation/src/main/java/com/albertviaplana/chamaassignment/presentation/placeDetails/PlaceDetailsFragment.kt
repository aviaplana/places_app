package com.albertviaplana.chamaassignment.presentation.placeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.databinding.PlaceDetailsFragmentBinding

class PlaceDetailsFragment: Fragment(R.layout.place_details_fragment) {
    private lateinit var binding: PlaceDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let {
            binding = PlaceDetailsFragmentBinding.bind(it)
        }

        return view
    }
}