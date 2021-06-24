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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.entity.DiagnosisItem
import com.keelim.nandadiagnosis.databinding.FragmentDiagnosisBinding

class DiagnosisFragment : Fragment() {
  private var _binding: FragmentDiagnosisBinding? = null
  private val binding get() = _binding!!
  private val data: ArrayList<DiagnosisItem> by lazy { ArrayList() }
  private val args by navArgs<DiagnosisFragmentArgs>()
  private var nav: Int = 0
  private val diagnosisAdapter = DiagnosisRecyclerViewAdapter(
    listener = { position ->
      findNavController()
        .navigate(
          DiagnosisFragmentDirections
            .actionDiagnosisFragmentToWebFragment("https://keelim.github.io/nandaDiagnosis/${nav + position + 1}.html")
        )
    }
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initArray()

    binding.list.adapter = diagnosisAdapter.apply {
      submitList(data)
    }
  }

  private fun initArray() {

    when (args.navigation) {
      "1" -> {
        nav = 0
        customAdd(nav, 11) // ok 1~12
      }
      "2" -> {
        nav = 12
        customAdd(nav, 22) // 13~23
      }
      "3" -> {
        nav = 23
        customAdd(nav, 32) // 24~33
      }
      "4" -> {
        nav = 33
        customAdd(nav, 51) // 34~52
      }
      "5" -> {
        nav = 52
        customAdd(nav, 86) // 53~87
      }
      "6" -> {
        nav = 87
        customAdd(nav, 97) // 88 ~98
      }
      "7" -> {
        nav = 98
        customAdd(nav, 108) // 99~109
      }
      "8" -> {
        nav = 109
        customAdd(nav, 123) // 110~124
      }
      "9" -> {
        nav = 124
        customAdd(nav, 129) // 125~130
      }
      "10" -> {
        nav = 130
        customAdd(nav, 167) // 131~168
      }
      "11" -> {
        nav = 168
        customAdd(nav, 178) // 169~179
      }
      "12" -> {
        nav = 179
        customAdd(nav, 205) // 180~206
      }
      "13" -> {
        nav = 204
        customAdd(nav, 223) // 207~236
      }
    }
  }

  private fun customAdd(startPoint: Int, finalPoint: Int) {
    val diagnosis = resources.getStringArray(R.array.diagnosis1)
    (startPoint..finalPoint).forEach { index ->
      data.add(DiagnosisItem(diagnosis[index], ""))
    }
  }
}
