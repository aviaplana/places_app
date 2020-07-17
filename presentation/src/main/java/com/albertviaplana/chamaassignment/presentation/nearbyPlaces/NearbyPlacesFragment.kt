package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.common.onReachEndListener
import com.albertviaplana.chamaassignment.presentation.databinding.NearbyPlacesFragmentBinding
import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class NearbyPlacesFragment : Fragment(R.layout.nearby_places_fragment) {
    lateinit var binding: NearbyPlacesFragmentBinding
    private val vm: NearbyPlacesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlacesView()

        vm.state.onEach {
            (binding.places.adapter as PlacesAdapter).setItems(it.places)
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            vm.event.consumeAsFlow()
                .collect{
                    when (it) {
                        is ShowDetails -> navigateToDetails()
                    }
                }
        }
    }

    private fun navigateToDetails() {
        val action = NearbyPlacesFragmentDirections.actionNearbyPlacesFragmentToPlaceDetailsFragment()
        findNavController().navigate(action)
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


    private fun initPlacesView() {
        binding.places.apply {
            adapter = PlacesAdapter {
                vm notify ClickedPlace(it)
            }
            onReachEndListener {
                vm notify LoadData
            }
        }
    }

}
