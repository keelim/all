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
package com.keelim.comssa.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ItemDataBinding
import com.keelim.comssa.databinding.ItemFeaturedDataBinding
import com.keelim.comssa.utils.dip
import com.keelim.comssa.utils.toAbbreviatedString
import com.keelim.comssa.utils.toDecimalFormatString
import com.keelim.data.model.Data
import com.keelim.data.model.FeaturedData

class HomeAdapter(
    val onDataClickListener: (Data) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<DataItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_SECTION_HEADER -> {
                TitleItemViewHolder(parent.context)
            }

            ITEM_VIEW_TYPE_FEATURED -> {
                FeaturedDataItemViewHolder(
                    ItemFeaturedDataBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ITEM_VIEW_TYPE_ITEM -> {
                DataItemViewHolder(
                    ItemDataBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemValue = data[position].value
        when {
            holder is TitleItemViewHolder && itemValue is String -> {
                holder.bind(itemValue)
            }

            holder is FeaturedDataItemViewHolder && itemValue is FeaturedData -> {
                holder.bind(itemValue)
            }

            holder is DataItemViewHolder && itemValue is Data -> {
                holder.bind(itemValue)
            }

            else -> throw RuntimeException("알 수 없는 ViewHolder 입니다.")
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position].value) {
        is String -> ITEM_VIEW_TYPE_SECTION_HEADER
        is FeaturedData -> ITEM_VIEW_TYPE_FEATURED
        else -> ITEM_VIEW_TYPE_ITEM
    }

    fun addData(featuredData: FeaturedData?, datas: List<Data>) {
        val newData = mutableListOf<DataItem>()

        featuredData?.let {
            newData += DataItem("🔥 요즘 핫한 영화")
            newData += DataItem(it)
        }

        newData += DataItem("🍿 이 영화들은 보셨나요?")
        newData += datas.map { DataItem(it) }

        data = newData
    }

    inner class TitleItemViewHolder(context: Context) : RecyclerView.ViewHolder(
        TextView(context).apply {
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.BLACK)
            setPadding(dip(12f), dip(6f), dip(12f), dip(6f))
        }
    ) {

        fun bind(item: String) {
            (itemView as? TextView)?.text = item
        }
    }

    inner class FeaturedDataItemViewHolder(private val binding: ItemFeaturedDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: FeaturedData) = with(binding) {
            posterImageView
                .load(item.data.posterUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                }
            scoreCountTextView.text = item.data.numberOfScore?.toAbbreviatedString()
            averageScoreTextView.text = item.data.averageScore?.toDecimalFormatString("0.0")

            item.latestReview?.let { review ->
                latestReviewLabelTextView.text =
                    if (review.userId.isNullOrBlank()) {
                        "🌟 따끈따끈한 후기"
                    } else {
                        "- ${review.userId!!.take(3)}*** -"
                    }

                latestReviewTextView.text = "\"${review.content}\""
            }

            root.setOnClickListener {
                (data[adapterPosition].value as? FeaturedData)?.data?.let {
                    onDataClickListener.invoke(it)
                }
            }
        }
    }

    inner class DataItemViewHolder(private val binding: ItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                (data[adapterPosition].value as? Data)?.let {
                    onDataClickListener.invoke(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(data: Data) = with(binding) {
            posterImageView.load(data.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }

            data.let {
                titleTextView.text = it.title
                additionalInformationTextView.text = "${it.releaseYear}·${it.country}"
            }
        }
    }

    data class DataItem(val value: Any)

    companion object {
        const val ITEM_VIEW_TYPE_SECTION_HEADER = 0
        const val ITEM_VIEW_TYPE_FEATURED = 1
        const val ITEM_VIEW_TYPE_ITEM = 2
    }
}
