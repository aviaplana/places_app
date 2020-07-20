package com.albertviaplana.chamaassignment.presentation.nearbyPlaces

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.common.onHide
import com.albertviaplana.chamaassignment.presentation.common.onReachEnd
import com.albertviaplana.chamaassignment.presentation.common.onScrollDown
import com.albertviaplana.chamaassignment.presentation.common.onScrollUp
import com.albertviaplana.chamaassignment.presentation.databinding.FiltersBottomSheetBinding
import com.albertviaplana.chamaassignment.presentation.databinding.NearbyPlacesFragmentBinding
import com.albertviaplana.chamaassignment.presentation.nearbyPlaces.viewModel.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel


@ExperimentalStdlibApi
@FlowPreview
@ExperimentalCoroutinesApi
class NearbyPlacesFragment : Fragment(R.layout.nearby_places_fragment) {
    private lateinit var binding: NearbyPlacesFragmentBinding
    private lateinit var filtersBinding: FiltersBottomSheetBinding
    private lateinit var bottomSheet: BottomSheetDialog
    private val viewModel: NearbyPlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
        if (savedInstanceState == null) viewModel reduce Created
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel reduce ViewCreated
        viewModel.state
            .onEach { updateView(it) }
            .launchIn(lifecycleScope)
    }

    private fun observeEvents() {
        viewModel.event
            .consumeAsFlow()
            .onEach {
                when (it) {
                    is ShowFilters -> showFilters()
                    is SetTypeFilterOptions -> setTypeFilterOptions(it.placeTypes)
                    is UpdateRadiusLabel -> updateRadiusLabel(it.radius)
                    is CheckPermissions -> requestLocationPermissions(requireContext())
                    is ShowFiltersButton -> showFiltersButton()
                    is HideFiltersButton -> hideFiltersButton()
                    is ShowDetails -> navigateToDetails(it.id)
                    is ShowError -> showError(it.message)
                }
            }.launchIn(lifecycleScope)
    }

    private fun setTypeFilterOptions(types: List<String>) {
        types.forEach {
            val chip = layoutInflater.inflate(R.layout.chip_type, filtersBinding.placeType, false)
            (chip as Chip).apply {
                text = it
                tag = it
            }
            filtersBinding.placeType.addView(chip)
        }
    }

    private fun updateRadiusLabel(radius: Int) {
        filtersBinding.radiusLabel.text = resources.getString(R.string.radius_meters, radius)
    }

    private fun updateView(vm: NearbyPlacesVM) {
        with(vm) {
            setPlaces(places)

            if (isLoading) showProgressBar()
            else hideProgressBar()

            setNoResultsMessage(vm)

            filtersBinding.radius.progress = filters.radiusProgress
            filtersBinding.onlyOpenNow.isChecked = filters.onlyOpenNow

            filtersBinding.placeType.children
                .map { it as Chip }
                .first { it.tag == filters.type }
                .also { it.isChecked = true }
        }
    }

    private fun setNoResultsMessage(vm: NearbyPlacesVM) {
        binding.noResultsMessage.visibility =
            if (!vm.isLoading && vm.places.isEmpty()) View.VISIBLE
            else View.GONE
    }

    private fun showFilters() {
        bottomSheet.show()
    }

    private fun setPlaces(places: List<PlaceVM>) {
        (binding.places.adapter as PlacesAdapter).setItems(places)
    }

    private fun showFiltersButton() {
        binding.filterFab.show()
    }

    private fun hideFiltersButton() {
        binding.filterFab.hide()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun requestLocationPermissions(context: Context) =
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .filter { ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED }
            .let {
                if (it.isNotEmpty()) {
                    requestPermissions(it.toTypedArray(), PERMISSIONS_REQUEST_LOCATION)
                } else {
                    viewModel reduce PermissionsGranted
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
                viewModel reduce PermissionsGranted
        } else {
            viewModel reduce PermissionsDenied
        }

    }

    private fun showError(message: String) {
        val duration = Toast.LENGTH_SHORT

        Toast.makeText(activity, message, duration)
                .show()
    }

    private fun navigateToDetails(productId: String) {
        val action = NearbyPlacesFragmentDirections.actionNearbyPlacesFragmentToPlaceDetailsFragment(
            productId
        )
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
            filtersBinding = FiltersBottomSheetBinding.inflate(inflater)
        }

        return view
    }

    private fun initView() {
        with(binding) {
            places.onReachEnd { viewModel reduce ScrollBottom }
                .onScrollDown { viewModel reduce ScrollDown }
                .onScrollUp { viewModel reduce ScrollUp }
                .adapter = PlacesAdapter {
                    viewModel reduce ClickedPlace(it)
                }
            filterFab.setOnClickListener {viewModel reduce ClickedFiltersButton }
        }

        initFiltersBottomView()
    }

    private fun initFiltersBottomView() {
        bottomSheet = BottomSheetDialog(requireContext())
            .apply {
                setContentView(filtersBinding.root)
                onHide {
                    with (filtersBinding) {
                        val placeType = placeType.children
                            .map { it as Chip }
                            .first { it.id == placeType.checkedChipId }
                            .tag.toString()

                        viewModel reduce FiltersDismissed(
                            radius.progress,
                            onlyOpenNow.isChecked,
                            placeType
                        )
                    }
                }
            }

        filtersBinding.apply {
            radius.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        bar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) viewModel reduce RadiusChanged(progress)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {}

                }
            )
        }

    }

    companion object {
        const val PERMISSIONS_REQUEST_LOCATION = 1
    }

}
