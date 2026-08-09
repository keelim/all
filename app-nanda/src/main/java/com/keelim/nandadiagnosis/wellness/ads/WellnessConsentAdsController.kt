package com.keelim.nandadiagnosis.wellness.ads

import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.keelim.nandadiagnosis.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class WellnessAdsState(
    val canRequestAds: Boolean = false,
    val privacyOptionsRequired: Boolean = false,
)

class WellnessConsentAdsController(
    private val activity: ComponentActivity,
) {
    private val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
    private val mutableState = MutableStateFlow(WellnessAdsState())
    private var mobileAdsInitializationRequested = false
    private var mobileAdsInitialized = false

    val state: StateFlow<WellnessAdsState> = mutableState.asStateFlow()

    fun requestConsent() {
        try {
            consentInformation.requestConsentInfoUpdate(
                activity,
                ConsentRequestParameters.Builder().build(),
                {
                    try {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                            publishState()
                        }
                    } catch (_: RuntimeException) {
                        publishState()
                    }
                },
                { publishState() },
            )
        } catch (_: RuntimeException) {
            publishState()
        }
    }

    fun showPrivacyOptions() {
        if (
            consentInformation.privacyOptionsRequirementStatus !=
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        ) {
            publishState()
            return
        }

        try {
            UserMessagingPlatform.showPrivacyOptionsForm(activity) { publishState() }
        } catch (_: RuntimeException) {
            publishState()
        }
    }

    private fun publishState() {
        val consentCanRequestAds = consentInformation.canRequestAds()
        if (consentCanRequestAds && !mobileAdsInitializationRequested) {
            mobileAdsInitializationRequested = true
            activity.lifecycleScope.launch(Dispatchers.IO) {
                runCatching {
                    val initializationConfig =
                        InitializationConfig.Builder(
                            BuildConfig.AD_NANDA_APPLICATION_ID,
                        ).build()
                    MobileAds.initialize(activity, initializationConfig) {
                        activity.lifecycleScope.launch {
                            mobileAdsInitialized = true
                            updateState(consentInformation.canRequestAds())
                        }
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        mobileAdsInitializationRequested = false
                        updateState(consentInformation.canRequestAds())
                    }
                }
            }
        }

        updateState(consentCanRequestAds)
    }

    private fun updateState(consentCanRequestAds: Boolean) {
        mutableState.value =
            wellnessAdsState(
                consentCanRequestAds = consentCanRequestAds,
                mobileAdsInitialized = mobileAdsInitialized,
                privacyOptionsRequirementStatus =
                    consentInformation.privacyOptionsRequirementStatus,
            )
    }
}

internal fun wellnessAdsState(
    consentCanRequestAds: Boolean,
    mobileAdsInitialized: Boolean,
    privacyOptionsRequirementStatus: ConsentInformation.PrivacyOptionsRequirementStatus,
): WellnessAdsState =
    WellnessAdsState(
        canRequestAds = consentCanRequestAds && mobileAdsInitialized,
        privacyOptionsRequired =
            privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED,
    )
