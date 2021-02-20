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
package com.keelim.nandadiagnosis.ui.main.search.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.ui.main.search.SearchRecyclerViewAdapter

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
  override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
    val view = recyclerView.findChildViewUnder(event.x, event.y)
    if (view != null) {
      return (recyclerView.getChildViewHolder(view) as SearchRecyclerViewAdapter.ViewHolder)
        .getItemDetails()
    }
    return null
  }
}
