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
package com.keelim.cnubus.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.services.TerminateService
import com.keelim.compose.ui.CircularIndeterminateProgressBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startService(Intent(this, TerminateService::class.java))
        observeLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, TerminateService::class.java))
    }

    private fun observeLoading() = viewModel.loading.observe(this){
        when(it){
            true -> binding.composeView.apply {
                bringToFront()
                setContent {
                    CircularIndeterminateProgressBar(true)
                }
            }
            false -> binding.composeView.apply {
                bringToFront()
                setContent {
                    CircularIndeterminateProgressBar(false)
                }
            }
        }
    }
}
