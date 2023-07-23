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
package com.keelim.cnubus.ui.screen.main

import android.Manifest.permission
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.common.extensions.toast
import com.keelim.commonAndroid.core.AppMainDelegator
import com.keelim.commonAndroid.core.AppMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: AppMainViewModel by viewModels()
    private val appMainDelegator by lazy { AppMainDelegator(this, viewModel) }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (navigationController.currentDestination?.id == R.id.tabFragment) {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("종료하시겠습니까?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialog, which ->
                        finish()
                    }
                    .setNegativeButton("Nope") { dialog, which -> }
                    .create()
                    .show()
            } else {
                navigationController.navigateUp()
            }
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        permissionLauncher.launch(appPermissions.toTypedArray())
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }
}
