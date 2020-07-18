package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.common.onReachEndListener
import com.albertviaplana.chamaassignment.presentation.databinding.NearbyPlacesFragmentBinding
import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class NearbyPlacesFragment : Fragment(R.layout.nearby_places_fragment) {
    lateinit var binding: NearbyPlacesFragmentBinding
    private val viewModel: NearbyPlacesViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!requestedPermissions(context)) viewModel reduce LoadData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlacesView()

        viewModel.state
            .onEach { updateView(it) }
            .launchIn(lifecycleScope)

        observeEvents()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.event.consumeAsFlow()
                .collect{
                    when (it) {
                        is ShowDetails -> navigateToDetails(it.id)
                        is ShowError -> showError(it.message)
                    }
                }
        }
    }

    private fun updateView(vm: NearbyPlacesVM) {
        with(vm) {
            setPlaces(places)

            if (isLoading) showProgressBar()
            else hideProgressBar()
        }
    }


    private fun setPlaces(places: List<PlaceVM>) {
        (binding.places.adapter as PlacesAdapter).setItems(places)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun requestedPermissions(context: Context) =
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .filter { ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED }
            .let {
                if (it.isNotEmpty()) {
                    requestPermissions(it.toTypedArray(), PERMISSIONS_REQUEST_LOCATION)
                    true
                } else {
                    false
                }
            }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_LOCATION &&
            !grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                viewModel reduce LoadData
        }

    }

    private fun showError(message: String) {
        val duration = Toast.LENGTH_SHORT

        Toast.makeText(activity, message, duration)
                .show()
    }

    private fun navigateToDetails(productId: String) {
        val action = NearbyPlacesFragmentDirections.actionNearbyPlacesFragmentToPlaceDetailsFragment(productId)
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
                viewModel reduce ClickedPlace(it)
            }
            onReachEndListener {
                viewModel reduce ScrollBottom
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_LOCATION = 1
    }

}
