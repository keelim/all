package com.keelim.nandadiagnosis.wellness.ads

import android.util.Log
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
import java.util.concurrent.atomic.AtomicBoolean

data class WellnessAdsState(
    val canRequestAds: Boolean = false,
    val privacyOptionsRequired: Boolean = false,
)

class WellnessConsentAdsController(
    private val activity: ComponentActivity,
) {
    private val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
    private val mutableState = MutableStateFlow(WellnessAdsState())
    private val mobileAdsInitializationRequested = AtomicBoolean(false)
    private var mobileAdsInitialized = false

    val state: StateFlow<WellnessAdsState> = mutableState.asStateFlow()

    fun requestConsent() {
        Log.d(TAG, "consent_info_update_requested")
        try {
            consentInformation.requestConsentInfoUpdate(
                activity,
                ConsentRequestParameters.Builder().build(),
                {
                    Log.d(TAG, "consent_info_update_succeeded")
                    try {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                            if (formError == null) {
                                Log.d(TAG, "consent_form_completed")
                            } else {
                                Log.w(TAG, "consent_form_failed")
                            }
                            publishState()
                        }
                    } catch (_: RuntimeException) {
                        Log.w(TAG, "consent_form_failed")
                        publishState()
                    }
                },
                {
                    Log.w(TAG, "consent_info_update_failed")
                    publishState()
                },
            )
        } catch (_: RuntimeException) {
            Log.w(TAG, "consent_info_update_failed")
            publishState()
        }
    }

    fun showPrivacyOptions() {
        if (
            consentInformation.privacyOptionsRequirementStatus !=
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        ) {
            Log.d(TAG, "privacy_options_not_required")
            publishState()
            return
        }

        try {
            Log.d(TAG, "privacy_options_requested")
            UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                if (formError == null) {
                    Log.d(TAG, "privacy_options_completed")
                } else {
                    Log.w(TAG, "privacy_options_failed")
                }
                publishState()
            }
        } catch (_: RuntimeException) {
            Log.w(TAG, "privacy_options_failed")
            publishState()
        }
    }

    private fun publishState() {
        val consentCanRequestAds = consentInformation.canRequestAds()
        if (consentCanRequestAds && mobileAdsInitializationRequested.compareAndSet(false, true)) {
            Log.d(TAG, "ads_init_requested")
            activity.lifecycleScope.launch(Dispatchers.IO) {
                runCatching {
                    val initializationConfig =
                        InitializationConfig.Builder(
                            BuildConfig.AD_NANDA_APPLICATION_ID,
                        ).build()
                    MobileAds.initialize(activity, initializationConfig) {
                        activity.lifecycleScope.launch {
                            mobileAdsInitialized = true
                            Log.d(TAG, "ads_init_completed")
                            updateState(consentInformation.canRequestAds())
                        }
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        mobileAdsInitializationRequested.set(false)
                        Log.w(TAG, "ads_init_failed")
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

private const val TAG = "WellnessAds"
