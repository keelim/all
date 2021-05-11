/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.common

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class DataBindingViewHolder<T>(val binding: ViewBinding, private val viewModel: ViewModel) : RecyclerView.ViewHolder(binding.root) {
//    fun bind(item: T){
//        binding.setVariable(BR.item, item)
//        binding.setVariable(BR.viewModel, viewModel)
//        binding.executePendingBindings()
//    }
}
