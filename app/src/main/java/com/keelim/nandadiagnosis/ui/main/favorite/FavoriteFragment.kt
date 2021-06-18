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
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.common.toast
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
  private var _binding: FragmentFavoriteBinding? = null
  private val binding get() = _binding!!
  private val adapter by lazy { FavoriteAdapter() }
  private lateinit var db: AppDatabaseV2
  private val scope = MainScope()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    scope.cancel()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    db = AppDatabaseV2.getInstance(requireContext())!!
    with(binding) {
      favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
      favoriteRecycler.adapter = adapter
    }
    initData()
  }

  private fun initData() {
    scope.launch {
      val items = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
        db.dataDao.favorites()
      }

      if (items.isEmpty()) {
        toast("아직 관심으로 지정한 영역이 없습니다.")
      }

      CoroutineScope(Dispatchers.Main).launch {
        if (items.isEmpty()) {
          binding.favoriteRecycler.visibility = View.GONE
        } else {
          adapter.submitList(items)
          adapter.notifyDataSetChanged()
        }
      }
    }
  }
}
