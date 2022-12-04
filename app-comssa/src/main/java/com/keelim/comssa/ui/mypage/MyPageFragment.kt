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
package com.keelim.comssa.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.R
import com.keelim.data.model.ReviewedData
import com.keelim.comssa.databinding.FragmentMyPageBinding
import com.keelim.comssa.extensions.dip
import com.keelim.comssa.extensions.toGone
import com.keelim.comssa.extensions.toVisible
import com.keelim.comssa.ui.home.GridSpacingItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var binding: FragmentMyPageBinding
    private val navController by lazy {
        findNavController()
    }

    private val myPageAdapter = MyPageAdapter(onDataClickListener = {
        navController.navigate(
            MyPageFragmentDirections.actionMyPageFragmentToReviewsFragment(it)
        )
    })

    private val viewModel by viewModels<MyPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)
        initViews()
        initFlow()
    }

    private fun showLoadingIndicator() {
        binding.progressBar.toVisible()
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.toGone()
    }

    private fun showNoDataDescription(message: String) {
        binding.recyclerView.toGone()
        binding.descriptionTextView.toVisible()
        binding.descriptionTextView.text = message
    }

    private fun showErrorDescription(message: String) {
        binding.recyclerView.toGone()
        binding.descriptionTextView.toVisible()
        binding.descriptionTextView.text = message
    }

    private fun showReviewedData(reviewedData: List<ReviewedData>) {
        myPageAdapter.apply {
            this.reviewedDatas = reviewedData
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding.recyclerView.apply {
            adapter = myPageAdapter
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(3, dip(6f)))
        }

        (binding.recyclerView.adapter as? MyPageAdapter)?.apply {
            onDataClickListener = { data ->
                val action = MyPageFragmentDirections.actionMyPageFragmentToReviewsFragment(data)
                findNavController().navigate(action)
            }
        }
    }

    private fun initFlow() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.reviewedData.collectLatest {
                    showLoadingIndicator()
                    if (it.isEmpty()) {
                        showNoDataDescription("아직 리뷰한 영화가 없어요.\n홈 탭을 눌러 영화를 리뷰해보세요 🙌")
                    } else {
                        showReviewedData(it)
                        hideLoadingIndicator()
                    }
                }
            }
        }
    }
}
