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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
//    private val viewModel: CategoryViewModel by activityViewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val binding = DataBindingUtil.inflate<FragmentCategoryBinding>(inflater, R.layout.fragment_category, container, false)
      .apply {
        card1.setOnClickListener { showDialog("1") }
        card2.setOnClickListener { showDialog("2") }
        card3.setOnClickListener { showDialog("3") }
        card4.setOnClickListener { showDialog("4") }
        card5.setOnClickListener { showDialog("5") }
        card6.setOnClickListener { showDialog("6") }
        card7.setOnClickListener { showDialog("7") }
        card8.setOnClickListener { showDialog("8") }
        card9.setOnClickListener { showDialog("9") }
        card10.setOnClickListener { showDialog("10") }
        card11.setOnClickListener { showDialog("11") }
        card12.setOnClickListener { showDialog("12") }
        card13.setOnClickListener { showDialog("13") }
      }
//    serveCard()
//

    return binding.root
  }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val binding = FragmentCategoryBinding.bind(view)
//
//
// //
//    }

//    private fun observeCard() {
//        viewModel.dialog.eventObserve(this) { num ->
//            AlertDialog.Builder(requireActivity())
//                    .setMessage("이 기능은 데이터를 사용할 수 있습니다.")
//                    .setCancelable(false)
//                    .setPositiveButton("예") { _, _ -> intentList(num) }
//                    .setNegativeButton("아니오") { _, _ ->
//                        Toast.makeText(requireActivity(), "아니오 선택했습니다.", Toast.LENGTH_LONG).show()
//                    }
//                    .create()
//                    .show()
//        }
//    }

  private fun showDialog(num: String) { // 데이터를 사용하는 페이지 이니 조심하라는 문구
    AlertDialog.Builder(requireActivity())
      .setMessage("이 기능은 데이터를 사용할 수 있습니다.")
      .setCancelable(false)
      .setPositiveButton("예") { _, _ ->
        intentList(num)
      }
      .setNegativeButton("아니오") { _, _ ->
        requireActivity().toast("아니오를 선택하였습니다.")
      }
      .create()
      .show()
  }

  private fun intentList(num: String) {
    Intent(requireActivity(), DiagnosisActivity::class.java).apply {
      putExtra("extra", num)
      startActivity(this)
    }
  }
}
