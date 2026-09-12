package com.keelim.nandadiagnosis.ui.screen.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.common.platform.appshell.AppShellState
import com.keelim.common.platform.privacy.PrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacySettingsRepository
import com.keelim.common.platform.privacy.PrivacyState
import com.keelim.commonAndroid.platform.privacy.BiometricPrivacyAuthenticator
import com.keelim.composeutil.component.appshell.KeelimAppShell
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.theme.NandaTheme
import com.keelim.nandadiagnosis.wellness.ads.WellnessConsentAdsController
import com.keelim.nandadiagnosis.wellness.ui.WellnessRoute
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject lateinit var privacySettingsRepository: PrivacySettingsRepository
    @Inject lateinit var privacyController: PrivacyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adsController = WellnessConsentAdsController(this)
        val authenticator = BiometricPrivacyAuthenticator(
            activity = this,
            title = getString(R.string.platform_unlock_title),
            subtitle = getString(R.string.platform_unlock_subtitle),
        )
        setContent {
            val adsState by adsController.state.collectAsStateWithLifecycle()
            val settings by privacySettingsRepository.observe().collectAsStateWithLifecycle(
                initialValue = PrivacySettings.Disabled,
            )
            val isUnlocked by privacyController.isUnlocked.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(adsController) {
                adsController.requestConsent()
            }
            NandaTheme {
                KeelimAppShell(
                    appState = AppShellState(),
                    privacyState = PrivacyState(settings, isUnlocked),
                    snackbarHostState = snackbarHostState,
                    onUnlockRequest = {
                        lifecycleScope.launch {
                            privacyController.onAuthenticationResult(authenticator.authenticate())
                        }
                    },
                    onBackgrounded = { privacyController.onBackgrounded(settings) },
                    onForegrounded = { privacyController.onForegrounded(settings) },
                ) {
                    WellnessRoute(
                        canRequestAds = adsState.canRequestAds,
                        privacyOptionsRequired = adsState.privacyOptionsRequired,
                        onShowPrivacyOptions = adsController::showPrivacyOptions,
                    )
                }
            }
        }
    }
}
