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
package com.keelim.cnubus.ui.setting.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentMyPageBinding
import com.keelim.common.base.BaseFragment
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.common.extensions.snak
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>(
) {
    override val layoutResourceId: Int = R.layout.fragment_my_page
    override val viewModel: MyPageViewModel by viewModels()

    private val historyAdapter by lazy {
        MyPageHistoryAdapter { history ->
            viewModel.deleteHistory(history)
            viewModel.init()
            viewModel.getAllHistories()
        }
    }

    override fun initBeforeBinding() {
        initData()
        initViews()
    }

    override fun initBinding() {
        observeState()
    }

    override fun initAfterBinding() = Unit

    private fun initData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() = with(binding) {
        ivEditBtn.setOnClickListener {
            findNavController().navigate(R.id.userEditDialog)
        }
        recyclerHistory.run {
            setHasFixedSize(true)
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
        }
        coverHistoryDetailButton.setOnClickListener {
            Snackbar.make(binding.root, "전부 지우기겠습니까?", Snackbar.LENGTH_SHORT).run {
                setAction("Ok") {
                    viewModel?.let {
                        it.deleteHistoryAll()
                        it.getAllHistories()
                    }
                }
                show()
            }
        }
    }

    private fun observeState() = lifecycleScope.launch {
        repeatCallDefaultOnStarted {
            viewModel.getAllHistories().collect{
                if (it.isEmpty()) {
                    binding.root.snak("저장된 기록이 없습니다.")
                } else{
                    historyAdapter.submitList(it)
                }
            }
        }
    }
}
