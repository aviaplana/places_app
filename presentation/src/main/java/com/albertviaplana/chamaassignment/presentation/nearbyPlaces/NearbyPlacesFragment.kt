package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.databinding.NearbyPlacesFragmentBinding
import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.NearbyPlacesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class NearbyPlacesFragment : Fragment(R.layout.nearby_places_fragment) {
    lateinit var binding: NearbyPlacesFragmentBinding
    private val vm: NearbyPlacesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPlacesAdapter()

        vm.state.filterNotNull().onEach {
            (binding.places.adapter as PlacesAdapter).setItems(it.places)
        }.launchIn(lifecycleScope)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let {
            binding = NearbyPlacesFragmentBinding.bind(it)
        }

        return view
    }


    private fun setPlacesAdapter() {
        binding.places.adapter = PlacesAdapter()
    }
}