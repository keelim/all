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
package com.keelim.cnubus.ui.screen.root.swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivitySwipeBinding
import com.keelim.data.model.gps.Location
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySwipeBinding
    private val mode by lazy { intent.getStringExtra("mode") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySwipeBinding>(
            this,
            R.layout.activity_swipe,
        ).apply {
        }.also {
            binding = it
        }
        initFlow()
        initData()
    }

    private fun initFlow() {
    }
    private fun initData() {
    }

    private fun handleMigrateSuccess(data: List<Location>) {
        if (data.isEmpty()) {
        } else {
        }
    }
}
