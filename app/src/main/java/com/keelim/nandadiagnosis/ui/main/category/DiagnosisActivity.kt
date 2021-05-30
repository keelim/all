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

import android.content.Intent
import android.os.Bundle
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.base.BaseActivity
import com.keelim.nandadiagnosis.data.model.DiagnosisItem
import com.keelim.nandadiagnosis.databinding.ActivityDiagnosisBinding
import com.keelim.nandadiagnosis.ui.WebActivity
import timber.log.Timber

class DiagnosisActivity : BaseActivity() {
  private var data: ArrayList<DiagnosisItem> = ArrayList()
  private var nav = 0
  private val binding: ActivityDiagnosisBinding by binding(R.layout.activity_diagnosis)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initArray()
    Timber.d("sample data ${data[0].diagnosis}")
    binding.list.adapter = DiagnosisRecyclerViewAdapter().apply {
      setDiagnosisItem(data)
      listener = object : DiagnosisRecyclerViewAdapter.OnSearchItemClickListener {
        override fun onSearchItemClick(position: Int) {
          goWeb(nav + position + 1)
        }
      }
    }
  }

  private fun goWeb(total: Int) {
    Intent(this, WebActivity::class.java).apply {
      putExtra("URL", "https://keelim.github.io/nandaDiagnosis/$total.html")
      startActivity(this)
    }
  }

  private fun initArray() {
    val array1 = resources.getStringArray(R.array.diagnosis1)
    val mapper = HashMap<Int, Pair<Int, Int>>().apply {
      this[0] = Pair(0, 11)
      this[1] = Pair(12, 22)
      this[2] = Pair(23, 32)
      this[3] = Pair(33, 51)
      this[4] = Pair(52, 86)
      this[5] = Pair(87, 97)
      this[6] = Pair(98, 108)
      this[7] = Pair(109, 123)
      this[8] = Pair(124, 129)
      this[9] = Pair(130, 167)
      this[10] = Pair(168, 178)
      this[11] = Pair(179, 205)
      this[12] = Pair(204, 223)
    }

    val pair = mapper[intent.getStringExtra("extra")!!.toInt()] ?: Pair(0, 0)
    customAdd(pair.first, pair.second, array1)
  }

  private fun customAdd(startPoint: Int, finalPoint: Int, array1: Array<String>) {
    for (i in startPoint..finalPoint) {
      data.add(DiagnosisItem(array1[i], ""))
    }
  }
}
