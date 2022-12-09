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
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.services.TerminateService
import com.keelim.cnubus.worker.BusWorker
import com.keelim.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    private val appPermissions: List<String> = buildList {
        add(permission.ACCESS_FINE_LOCATION)
        add(permission.ACCESS_COARSE_LOCATION)
        add(permission.READ_EXTERNAL_STORAGE)
        add(permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(permission.POST_NOTIFICATIONS)
            add(permission.READ_MEDIA_IMAGES)
            add(permission.READ_MEDIA_VIDEO)
            add(permission.READ_MEDIA_AUDIO)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter { appPermissions.contains(it.key) }
            if (responsePermissions.filter { it.value }.size == appPermissions.size) {
                toast("권한이 확인되었습니다.")
            }
        }

    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startService(Intent(this, TerminateService::class.java))
        permissionLauncher.launch(appPermissions.toTypedArray())
        initViews()
        observeState()
        startWork()
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

    private fun startWork(start: Int = 10) {
        val loopRequest = PeriodicWorkRequest
            .Builder(BusWorker::class.java, 1, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .setInputData(
                workDataOf(
                    BusWorker.START to start
                )
            )
            .build()
        registerWork(loopRequest)
    }

    private fun registerWork(work: WorkRequest) {
        workManager.apply {
            cancelAllWork()
            enqueue(work)
        }
    }
}
