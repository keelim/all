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
package com.keelim.map3.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.keelim.common.extensions.loadAsync
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.data.model.gps.Location
import com.keelim.map.R
import com.keelim.map.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        observeState()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(intent)
        }
    }

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
         * flowWithLifecycle ??? ?????? ?????? ????????? ?????? ?????? ????????? ?????? ??????????
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
                        Snackbar.make(
                            binding.root,
                            if (state.data.isEmpty()) {
                                "???????????? ??????????????????."
                            } else {
                                "?????? ???????????? ?????? ???????????????."
                            },
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is DetailState.UnInitialized -> {}
                    else -> {}
                }
            }
    }
}
