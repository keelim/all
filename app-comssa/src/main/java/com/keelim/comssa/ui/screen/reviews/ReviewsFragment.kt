/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.comssa.ui.screen.reviews

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.databinding.FragmentReviewBinding
import com.keelim.data.model.Data
import com.keelim.data.model.DataReviews
import com.keelim.data.model.Review
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        fetchReviews()
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

    private fun showMovieInformation(movie: Data) {
        binding.recyclerView.adapter = ReviewsAdapter(
            movie,
            onReviewSubmitButtonClickListener = { content, score ->
                requestAddReview(content, score)
                hideKeyboard()
            },
            onReviewDeleteButtonClickListener = { review ->
                showDeleteConfirmDialog(review)
            },
        )
    }

    private fun showDeleteConfirmDialog(review: Review) {
        AlertDialog.Builder(requireContext())
            .setMessage("정말로 리뷰를 삭제하시겠어요?")
            .setPositiveButton("삭제할래요") { _, _ ->
                requestRemoveReview(review)
            }
            .setNegativeButton("안할래요") { _, _ -> }
            .show()
    }

    private fun showReviews(reviews: DataReviews) {
        binding.recyclerView.isVisible = true
        binding.errorDescriptionTextView.isVisible = false
        (binding.recyclerView.adapter as? ReviewsAdapter)?.apply {
            this.myReview = reviews.myReview
            this.reviews = reviews.othersReview
            notifyDataSetChanged()
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initViews() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private var dataReviews: DataReviews = DataReviews(null, emptyList())

    private fun requestAddReview(content: String, score: Float) {
        try {
            showLoadingIndicator()
            showReviews(dataReviews.copy(myReview = viewModel.review.value))
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorToast("리뷰 등록을 실패했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    }

    private fun requestRemoveReview(review: Review) {
        try {
            showLoadingIndicator()
            viewModel.deleteReview(review)
            showReviews(dataReviews.copy(myReview = null))
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorToast("리뷰 삭제를 실패했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    }

    private fun fetchReviews() {
        try {
            showLoadingIndicator()
            showReviews(viewModel.reviewList.value!!)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorDescription("에러가 발생했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    }
}
