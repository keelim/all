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
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding
import com.keelim.nandadiagnosis.utils.MaterialDialog
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.message
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.negativeButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.positiveButton

class CategoryFragment : Fragment() {
  private var _binding: FragmentCategoryBinding? = null
  private val binding get() = _binding!!
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.card1.setOnClickListener { showDialog("1") }
    binding.card2.setOnClickListener { showDialog("2") }
    binding.card3.setOnClickListener { showDialog("3") }
    binding.card4.setOnClickListener { showDialog("4") }
    binding.card5.setOnClickListener { showDialog("5") }
    binding.card6.setOnClickListener { showDialog("6") }
    binding.card7.setOnClickListener { showDialog("7") }
    binding.card8.setOnClickListener { showDialog("8") }
    binding.card9.setOnClickListener { showDialog("9") }
    binding.card10.setOnClickListener { showDialog("10") }
    binding.card11.setOnClickListener { showDialog("11") }
    binding.card12.setOnClickListener { showDialog("12") }
    binding.card13.setOnClickListener { showDialog("13") }
  }

  fun showDialog(num: String) { // 데이터를 사용하는 페이지 이니 조심하라는 문구
    MaterialDialog.createDialog(requireContext()) {
      message("이 기능은 데이터를 사용할 수 있습니다.")
      positiveButton(getString(R.string.ok)) {
        findNavController().navigate(
          CategoryFragmentDirections.actionNavigationCategoryToDiagnosisFragment(num)
        )
      }
      negativeButton(getString(R.string.cancel))
    }.show()
  }
}
