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
package com.keelim.comssa.ui.reviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ItemDataInformationBinding
import com.keelim.comssa.databinding.ItemMyReviewBinding
import com.keelim.comssa.databinding.ItemReviewBinding
import com.keelim.comssa.databinding.ItemReviewFormBinding
import com.keelim.comssa.utils.toAbbreviatedString
import com.keelim.comssa.utils.toDecimalFormatString
import com.keelim.data.model.Data
import com.keelim.data.model.Review

class ReviewsAdapter(
    private val movie: Data,
    var onReviewSubmitButtonClickListener: ((content: String, score: Float) -> Unit),
    var onReviewDeleteButtonClickListener: ((Review) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var myReview: Review? = null
    var reviews: List<Review> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> {
                MovieInformationViewHolder(
                    ItemDataInformationBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ITEM_VIEW_TYPE_ITEM -> {
                ReviewViewHolder(parent)
            }

            ITEM_VIEW_TYPE_REVIEW_FORM -> {
                ReviewFormViewHolder(
                    ItemReviewFormBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ITEM_VIEW_TYPE_MY_REVIEW -> {
                MyReviewViewHolder(
                    ItemMyReviewBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> throw RuntimeException("??? ??? ?????? ViewType ?????????.")
        }

    override fun getItemCount(): Int = 2 + reviews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieInformationViewHolder -> {
                holder.bind(movie)
            }

            is ReviewViewHolder -> {
                holder.bind(reviews[position - 2])
            }

            is MyReviewViewHolder -> {
                myReview ?: return
                holder.bind(myReview!!)
            }

            is ReviewFormViewHolder -> Unit
            else -> throw RuntimeException("??? ??? ?????? ViewHolder ?????????.")
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            1 -> {
                if (myReview == null) {
                    ITEM_VIEW_TYPE_REVIEW_FORM
                } else {
                    ITEM_VIEW_TYPE_MY_REVIEW
                }
            }

            else -> ITEM_VIEW_TYPE_ITEM
        }

    class MovieInformationViewHolder(private val binding: ItemDataInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Data) {
            binding.posterImageView.load(item.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }

            item.let {
                binding.averageScoreTextView.text =
                    "?????? ${it.averageScore?.toDecimalFormatString("0.0")} " +
                    "(${it.numberOfScore?.toAbbreviatedString()})"
                binding.titleTextView.text = it.title
                binding.additionalInformationTextView.text = "${it.releaseYear}??${it.country}"
                binding.relationsTextView.text = "??????: ${it.director}\n?????????: ${it.actors}"
                binding.genreChipGroup.removeAllViews()
                it.genre?.split(" ")?.forEach { genre ->
                    binding.genreChipGroup.addView(
                        Chip(binding.root.context).apply {
                            isClickable = false
                            text = genre
                        }
                    )
                }
            }
        }
    }

    inner class ReviewViewHolder(
        parent: ViewGroup,
        private val binding: ItemReviewBinding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Review) {
            item.let {
                binding.authorIdTextView.text = "${it.userId?.take(3)}***"
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    @SuppressLint("SetTextI18n")
    inner class ReviewFormViewHolder(private val binding: ItemReviewFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.submitButton.setOnClickListener {
                onReviewSubmitButtonClickListener.invoke(
                    binding.reviewFieldEditText.text.toString(),
                    binding.ratingBar.rating
                )
            }
            binding.reviewFieldEditText.addTextChangedListener { editable ->
                binding.contentLimitTextView.text = "(${editable?.length ?: 0}/50)"
                binding.submitButton.isEnabled = (editable?.length ?: 0) >= 5
            }
        }
    }

    inner class MyReviewViewHolder(private val binding: ItemMyReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteButton.setOnClickListener {
                onReviewDeleteButtonClickListener.invoke(myReview!!)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Review) {
            item.let {
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
        const val ITEM_VIEW_TYPE_REVIEW_FORM = 2
        const val ITEM_VIEW_TYPE_MY_REVIEW = 3
    }
}
