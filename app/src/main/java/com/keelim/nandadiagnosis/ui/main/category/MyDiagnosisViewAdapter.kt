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
package com.keelim.nandadiagnosis.ui.main.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import com.keelim.nandadiagnosis.model.DiagnosisItem
import java.util.ArrayList

class MyDiagnosisViewAdapter(private val diagnosisItems: ArrayList<DiagnosisItem>?) : BaseAdapter() {

    override fun getCount(): Int {
        return diagnosisItems!!.size
    }

    override fun getItem(position: Int): Any {
        return diagnosisItems!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding: ItemListviewBinding = ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.diagnosisItem.text = diagnosisItems!![position].diagnosis
        binding.diagnosisDes.text = diagnosisItems[position].diagnosis

        return binding.root
    }
}
