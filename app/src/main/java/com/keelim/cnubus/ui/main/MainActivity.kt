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
package com.keelim.cnubus.ui.main

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.services.TerminateService
import com.keelim.common.extensions.toast
import com.keelim.compose.ui.CircularIndeterminateProgressBar
import com.keelim.ui_setting.ui.theme.CnubusTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val locationPermissions = arrayOf(
        permission.ACCESS_FINE_LOCATION,
        permission.ACCESS_COARSE_LOCATION,
    )

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                it.key == permission.ACCESS_FINE_LOCATION ||
                    it.key == permission.ACCESS_COARSE_LOCATION
            }
            if (responsePermissions.filter { it.value }
                .size == locationPermissions.size
            ) {
                toast("권한이 확인되었습니다.")
            }
        }

    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startService(Intent(this, TerminateService::class.java))
        locationPermissionLauncher.launch(locationPermissions)
        initViews()
        observeState()
    }

    override fun onStop() {
        super.onStop()
        stopService(Intent(this, TerminateService::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() = with(binding) {
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if (destination.id == R.id.stationArrivalsFragment) {
            }
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.loading
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect {
                binding.composeView.run {
                    bringToFront()
                    setContent {
                        CnubusTheme {
                            CircularIndeterminateProgressBar(it)
                        }
                    }
                }
            }
    }

    override fun onBackPressed() {
        if (navigationController.currentDestination?.id == R.id.tabFragment) {
            MaterialAlertDialogBuilder(this)
                .setTitle("종료하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, which ->
                    super.onBackPressed()
                }
                .setNegativeButton("Nope") { dialog, which -> }
                .create()
                .show()
        } else {
            navigationController.navigateUp()
        }
    }
}
