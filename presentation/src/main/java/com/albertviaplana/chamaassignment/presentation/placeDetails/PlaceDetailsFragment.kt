package com.albertviaplana.chamaassignment.presentation.placeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.albertviaplana.chamaassignment.presentation.R
import com.albertviaplana.chamaassignment.presentation.common.getIsOpenText
import com.albertviaplana.chamaassignment.presentation.databinding.PlaceDetailsFragmentBinding
import com.albertviaplana.chamaassignment.presentation.databinding.ReviewCardBinding
import com.albertviaplana.chamaassignment.presentation.placeDetails.viewModel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class PlaceDetailsFragment: Fragment(R.layout.place_details_fragment) {
    private lateinit var binding: PlaceDetailsFragmentBinding

    private val viewModel: PlaceDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val placeId = arguments?.getString("placeId")

        viewModel reduce LoadData(placeId)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let {
            binding = PlaceDetailsFragmentBinding.bind(it)
            binding.photos.adapter = PhotosAdapter()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEvents()
    }

    private fun observeState() {
        viewModel.state
            .onEach {
                when (it) {
                    is Loading -> showProgressBar()
                    is DataLoaded -> {
                        hideProgressBar()
                        bindViewData(it.data)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.event.consumeAsFlow()
                .collect{
                    when (it) {
                        is ShowError -> showError(it.message)
                    }
                }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun bindViewData(vm: DetailsVM) {
        with(binding) {
            name.text = vm.name
            address.text = vm.address
            rating.rating = vm.rating
            website.text = vm.webSite
            mapsUrl.text = vm.mapsUrl
            phoneNumber.text = vm.phoneNumber
            placeIsOpen.text = vm.openStatus.getIsOpenText(root.context)
            (photos.adapter as PhotosAdapter).setPhotos(vm.photos)
            vm.reviews?.forEachIndexed { index, review ->
                addReviewCellView(review, reviews, index%2 == 0) }
        }
    }

    private fun addReviewCellView(review: ReviewVM, parent: ViewGroup, darkBackground: Boolean) {
        ReviewCardBinding.inflate(layoutInflater).apply {
            name.text = review.name
            date.text = review.date
            rating.rating = review.rating
            setReviewText(text, review.text)
            profilePhoto.load(review.profilePhotoUrl)
        }.also { parent.addView(it.root) }
    }

    private fun setReviewText(view: TextView, text: String) {
        if (text.isEmpty()) {
            view.visibility = View.GONE
        } else {
            view.text = text
            view.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        val duration = Toast.LENGTH_SHORT

        Toast.makeText(activity, message, duration)
            .show()
    }
}