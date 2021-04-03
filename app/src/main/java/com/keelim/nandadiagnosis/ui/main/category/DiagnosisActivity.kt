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
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.ActivityDiagnosisBinding
import com.keelim.nandadiagnosis.data.DiagnosisItem
import com.keelim.nandadiagnosis.ui.WebActivity
import java.util.ArrayList

class DiagnosisActivity : AppCompatActivity() {
  private var arrayList: ArrayList<DiagnosisItem>? = ArrayList()
  private var nav = 0
  private lateinit var binding: ActivityDiagnosisBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDiagnosisBinding.inflate(layoutInflater)
    setContentView(binding.root)
    arrayListSetting()

//        binding.list.adapter = MyDiagnosisViewAdapter(arrayList)
//        binding.list.setOnItemClickListener { _, _, i, _ ->
//            goWeb(nav + i + 1)
//        }

    binding.list.adapter = DiagnosisRecyclerViewAdapter().apply {
      setDiagnosisItem(arrayList!!.toMutableList())
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

  private fun arrayListSetting() {
    val array1 = resources.getStringArray(R.array.diagnosis1)
    when (intent.getStringExtra("extra")) {
      "1" -> {
        nav = 0
        customAdd(nav, 11, array1) // ok 1~12
      }
      "2" -> {
        nav = 12
        customAdd(nav, 22, array1) // 13~23
      }
      "3" -> {
        nav = 23
        customAdd(nav, 32, array1) // 24~33
      }
      "4" -> {
        nav = 33
        customAdd(nav, 51, array1) // 34~52
      }
      "5" -> {
        nav = 52
        customAdd(nav, 86, array1) // 53~87
      }
      "6" -> {
        nav = 87
        customAdd(nav, 97, array1) // 88 ~98
      }
      "7" -> {
        nav = 98
        customAdd(nav, 108, array1) // 99~109
      }
      "8" -> {
        nav = 109
        customAdd(nav, 123, array1) // 110~124
      }
      "9" -> {
        nav = 124
        customAdd(nav, 129, array1) // 125~130
      }
      "10" -> {
        nav = 130
        customAdd(nav, 167, array1) // 131~168
      }
      "11" -> {
        nav = 168
        customAdd(nav, 178, array1) // 169~179
      }
      "12" -> {
        nav = 179
        customAdd(nav, 205, array1) // 180~206
      }
      "13" -> {
        nav = 204
        customAdd(nav, 223, array1) // 207~236
      }
    }
  }

  private fun customAdd(startPoint: Int, finalPoint: Int, array1: Array<String>) {
    for (i in startPoint..finalPoint) {
      arrayList!!.add(DiagnosisItem(array1[i], ""))
    }
  }

  private fun getDiagnosis(number: Int): List<NandaEntity> { // 데이터베이스 가져와서 검색하기
    return AppDatabase.getInstance(this)!!.dataDao().get(number)
  }
}
