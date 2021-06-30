package com.keelim.comssa.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private val arguments: ReviewsFragmentArgs by navArgs()
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val data by lazy { arguments.data }
    private val reviewAdapter = DataReviewsAdapter(data)
    private val viewModel: ReviewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeReviews()
        viewModel.fetchReview(data.id!!)
    }

    private fun showLoadingIndicator() {
        binding.progressBar.isVisible = true
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.isVisible = false
    }

    private fun showErrorDescription(message: String) {
        binding.recyclerView.isVisible = false
        binding.errorDescriptionTextView.isVisible = true
        binding.errorDescriptionTextView.text = message
    }

    private fun showReviews(reviews: List<Review>) {
        binding.recyclerView.isVisible = true
        binding.errorDescriptionTextView.isVisible = false
        binding.recyclerView.adapter = reviewAdapter.apply {
            this.reviews = reviews
            notifyDataSetChanged()
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun observeReviews() = viewModel.review.observe(viewLifecycleOwner, {
        try {
            showLoadingIndicator()
            showReviews(it)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorDescription("에러가 발생했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    })
}


