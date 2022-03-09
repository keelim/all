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
package com.keelim.cnubus.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivitySplashBinding
import com.keelim.cnubus.ui.main.MainActivity
import com.keelim.cnubus.utils.VerificationUtils
import com.keelim.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var verificationUtils: VerificationUtils

    override fun initBeforeBinding() = Unit
    override fun initDataBinding() {
        observeState()
    }

    override fun initAfterBinding() = Unit

    private fun observeState() = lifecycleScope.launch {
        viewModel.loading
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect {
                if (it) {
                    val isNotVerifiedApplication = if (BuildConfig.DEBUG) {
                        verificationUtils.isVerifiedDebug()
                    } else {
                        verificationUtils.isVerifiedRelease()
                    }
                    if (isNotVerifiedApplication.not()) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }
                    finish()
                }
            }
    }

    override fun onBackPressed() {}
}
