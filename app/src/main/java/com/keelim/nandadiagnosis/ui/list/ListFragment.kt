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
package com.keelim.nandadiagnosis.ui.list

import androidx.fragment.app.viewModels
import com.keelim.common.toast
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ListFragment : BaseFragment<ListFragmentViewModel, FragmentListBinding>() {
  override val viewModel: ListFragmentViewModel by viewModels()

  override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

  override fun observeData() = viewModel.nandaListState.observe(this) {
    when (it) {
      is NandaListState.Error -> {
        initViews(binding)
      }
      is NandaListState.Loading -> {
        handleLoadingState()
      }
      is NandaListState.Success -> {
        handleSuccessState(it)
      }
      is NandaListState.UnInitialized -> {
        handleErrorState()
      }
    }
  }

  private fun handleLoadingState() {
    TODO("Not yet implemented")
  }

  private fun handleSuccessState(state: NandaListState.Success) {
    toast("Produce entity")
  }

  private fun handleErrorState() {
    toast("에러가 발생했습니다.")
  }

  private fun initViews(binding: FragmentListBinding) = with(binding) {
    viewModel.fetchData()
  }
}
