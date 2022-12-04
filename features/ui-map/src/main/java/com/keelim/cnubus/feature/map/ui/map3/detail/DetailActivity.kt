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
package com.keelim.cnubus.feature.map.ui.map3.detail

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.data.model.gps.Location
import com.keelim.cnubus.feature.map.R
import com.keelim.cnubus.feature.map.databinding.ActivityDetailBinding
import com.keelim.common.base.BaseActivity
import com.keelim.common.extensions.loadAsync
import com.keelim.common.extensions.snack
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_detail
    override val viewModel: DetailViewModel by viewModels()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(intent)
        }
    }

    override fun initBeforeBinding() {
        handleIntent(intent)
    }

    override fun initDataBinding() {
        observeState()
    }

    override fun initAfterBinding() = Unit

    private fun handleIntent(intent: Intent) = with(binding) {
        val location: Location? by lazy { intent.getParcelableExtra("item") }
        location?.let {
            title.text = it.name
            imageDetail.loadAsync(it.imgUrl, null)
            tvLatlng.text = it.latLng.toString().slice(IntRange(0, 15))
        }
    }

    private fun observeState() = lifecycleScope.launch {
        /**
         * flowWithLifecycle 가 여러 개를 가지고 싶을 때는 어떻게 해야 하는가?
         */
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                when (state) {
                    is DetailState.Error -> {}
                    is DetailState.Loading -> {
                        binding.loading.toVisible()
                    }
                    is DetailState.Success -> {
                        binding.loading.toGone()
                        binding.root.snack(
                            if (state.data.isEmpty()) {
                                "데이터가 비어있습니다."
                            } else {
                                "현재 업데이트 준비 구간입니다."
                            }
                        )
                    }
                    is DetailState.UnInitialized -> {}
                }
            }
    }
}
