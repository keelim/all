/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.comssa.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.comssa.databinding.ActivityFavoriteBinding
import com.keelim.comssa.extensions.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
  private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater) }

  private val viewModel: FavoriteViewModel by viewModels()
  private val favoriteAdapter = FavoriteAdapter(
    click = { favorite, id ->
      viewModel.favorite(favorite, id)
    }
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    observeData()
    viewModel.fetchData()
  }
  private fun observeData() = viewModel.favoriteState.observe(this) {
    when (it) {
      is FavoriteState.UnInitialized -> {
        initViews()
      }
      is FavoriteState.Loading -> toast("데이터 로딩 중 입니다.")
      is FavoriteState.Success -> handleSuccess(it)
      is FavoriteState.Error -> toast("에러가 발생하였습니다. 뒤로가기를 눌러주세요")
    }
  }

  private fun initViews() = with(binding) {
    favoriteRecycler.adapter = favoriteAdapter
    favoriteRecycler.layoutManager = LinearLayoutManager(this@FavoriteActivity)
    favoriteAdapter.submitList(listOf())
  }

  private fun handleSuccess(state: FavoriteState.Success) {
    favoriteAdapter.submitList(state.data)
  }
}
