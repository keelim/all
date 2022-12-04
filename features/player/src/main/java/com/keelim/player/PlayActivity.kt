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
package com.keelim.player

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.common.util.repeatCallDefaultOnStarted
import com.keelim.player.databinding.ActivityPlayBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PlayActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPlayBinding.inflate(layoutInflater) }
    private val viewModel: PlayViewModel by viewModels()
    private val videoAdapter by lazy {
        VideoAdapter(
            click = { url, title ->
                supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                    (it as PlayerFragment).play(url, title)
                }
            }
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        initViews()
        observeState()
        getVideoList()
    }

    private fun initViews() = with(binding) {
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PlayActivity)
            adapter = videoAdapter
        }
    }

    private fun getVideoList() {
        viewModel.getVideo()
    }

    private fun observeState() = repeatCallDefaultOnStarted {
        viewModel.state.collect {
            when (it) {
                is PlayState.Error -> Unit
                is PlayState.Loading -> Unit
                is PlayState.Success -> videoAdapter.submitList(it.data)
                is PlayState.UnInitialized -> Unit
            }
        }
    }
}
