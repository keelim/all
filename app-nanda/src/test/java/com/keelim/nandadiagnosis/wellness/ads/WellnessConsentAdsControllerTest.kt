package com.keelim.nandadiagnosis.wellness.ads

import com.google.android.ump.ConsentInformation
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WellnessConsentAdsControllerTest : FunSpec({
    test("ads require consent and SDK initialization while privacy options follow UMP") {
        wellnessAdsState(
            consentCanRequestAds = true,
            mobileAdsInitialized = false,
            privacyOptionsRequirementStatus =
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED,
        ) shouldBe WellnessAdsState(
            canRequestAds = false,
            privacyOptionsRequired = true,
        )

        wellnessAdsState(
            consentCanRequestAds = false,
            mobileAdsInitialized = true,
            privacyOptionsRequirementStatus =
                ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED,
        ).canRequestAds shouldBe false

        wellnessAdsState(
            consentCanRequestAds = true,
            mobileAdsInitialized = true,
            privacyOptionsRequirementStatus =
                ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED,
        ) shouldBe WellnessAdsState(
            canRequestAds = true,
            privacyOptionsRequired = false,
        )
    }
})
