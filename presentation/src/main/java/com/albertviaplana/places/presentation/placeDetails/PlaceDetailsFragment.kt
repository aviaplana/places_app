package com.albertviaplana.places.presentation.placeDetails

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.albertviaplana.places.presentation.R
import com.albertviaplana.places.presentation.common.getIsOpenText
import com.albertviaplana.places.presentation.databinding.PlaceDetailsFragmentBinding
import com.albertviaplana.places.presentation.databinding.ReviewCardBinding
import com.albertviaplana.places.presentation.placeDetails.viewModel.*
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
        observeEvents()

        if (savedInstanceState == null) viewModel reduce Created(placeId)
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
        viewModel.event
            .consumeAsFlow()
            .onEach {
                when (it) {
                    is ShowError -> showError(it.message)
                }
            }.launchIn(lifecycleScope)
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
            phoneNumber.text = vm.phoneNumber
            placeIsOpen.text = vm.openStatus.getIsOpenText(root.context)
            if (vm.webSite.isNotEmpty()) setHyperLink(website, R.string.website_link, vm.webSite)
            if (vm.mapsUrl.isNotEmpty()) setHyperLink(mapsUrl, R.string.maps_link, vm.mapsUrl)
            (photos.adapter as PhotosAdapter).setPhotos(vm.photos)
            vm.reviews.forEach { addReviewCellView(it, reviews) }
        }
    }

    private fun setHyperLink(textView: TextView, @StringRes stringId: Int, url: String) {
        val linkMovementMethod = LinkMovementMethod.getInstance()
        textView.movementMethod = linkMovementMethod

        textView.text =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(resources.getString(stringId, url), HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                HtmlCompat.fromHtml(resources.getString(stringId, url), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

    }

    private fun addReviewCellView(review: ReviewVM, parent: ViewGroup) {
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