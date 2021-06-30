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
package com.keelim.comssa.ui.mypage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.ReviewedData
import com.keelim.comssa.databinding.ItemReviewedDataBinding
import com.keelim.comssa.extensions.toDecimalFormatString

class MyPageAdapter(
  var onDataClickListener: (Data) -> Unit
) : RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {
  var reviewedDatas: List<ReviewedData> = emptyList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder =
    ViewHolder(
      ItemReviewedDataBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)
    )

  override fun getItemCount(): Int = reviewedDatas.size

  override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int): Unit =
    holder.bind(reviewedDatas[position])

  inner class ViewHolder(private val binding: ItemReviewedDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
      binding.root.setOnClickListener {
        onDataClickListener.invoke(reviewedDatas[adapterPosition].data)
      }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ReviewedData) {
      Glide.with(binding.root)
        .load(item.data.posterUrl)
        .into(binding.posterImageView)

      binding.myScoreTextView.text = item.review.score?.toDecimalFormatString("0.0")
    }
  }
}
