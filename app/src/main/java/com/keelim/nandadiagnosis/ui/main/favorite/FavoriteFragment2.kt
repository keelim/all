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
package com.keelim.nandadiagnosis.ui.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.common.toast
import com.keelim.nandadiagnosis.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FavoriteFragment2 : Fragment() {
  private val favoriteAdapter = FavoriteAdapter()
  private val viewModel: FavoriteViewModel by viewModels()
  private var _binding: FragmentFavoriteBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFavoriteBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.fetchData()
    observeData()
  }

  private fun observeData() = lifecycleScope.launch {
    viewModel.favoriteState.collect {
      when (it) {
        is FavoriteListState.UnInitialized -> {
          initViews(binding)
        }
        is FavoriteListState.Success -> {
          handleSuccess(it)
        }
        is FavoriteListState.Loading -> {
          handleLoading()
        }
        is FavoriteListState.Error -> {
          handleError()
        }
      }
    }
  }

  private fun handleError() {
    requireActivity().toast("Error 로딩 화면에 에러가 표시 됩니다.")
  }

  private fun handleSuccess(state: FavoriteListState.Success) {
    requireActivity().toast("$state")
    Timber.d(" 데이터 화면 넘어가기 $state")
    favoriteAdapter.submitList(state.favoriteList)
  }

  private fun handleLoading() {
    requireActivity().toast("현재 데이터를 불러오는 중입니다.")
  }

  private fun initViews(binding: FragmentFavoriteBinding) = with(binding) {
    favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
    favoriteRecycler.adapter = favoriteAdapter
    viewModel.fetchData()
  }
}
