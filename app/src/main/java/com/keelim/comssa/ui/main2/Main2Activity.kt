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
package com.keelim.comssa.ui.main2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivityMain2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Main2Activity : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.reviewsFragment, R.id.myPageFragment)
        )
        setupActionBarWithNavController(navController(), appBarConfiguration)
        navView.setupWithNavController(navController())
    }

    private fun navController() = findNavController(R.id.nav_host_fragment)
}
