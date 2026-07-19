package com.keelim.nandadiagnosis.wellness.ads

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.keelim.common.extensions.findActivity
import com.keelim.nandadiagnosis.BuildConfig

@Composable
fun RoutineAdBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val isPreviewMode = LocalInspectionMode.current
    var bannerVisible by remember { mutableStateOf(true) }

    if (bannerVisible) {
        BoxWithConstraints(modifier = modifier) {
            val adWidth = maxWidth.value.toInt().coerceAtLeast(1)
            val adSize =
                remember(context, adWidth) {
                    AdSize.getLargeAnchoredAdaptiveBannerAdSize(context, adWidth)
                }
            key(adWidth) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth().height(adSize.height.dp),
                    factory = { viewContext ->
                        AdView(viewContext).apply {
                            if (!isPreviewMode) {
                                loadAd(
                                    BannerAdRequest.Builder(
                                        if (BuildConfig.DEBUG) {
                                            BuildConfig.AD_NANDA_TEST_BANNER_ID
                                        } else {
                                            BuildConfig.AD_NANDA_BANNER_ID
                                        },
                                        adSize,
                                    ).build(),
                                    object : AdLoadCallback<BannerAd> {
                                        override fun onAdFailedToLoad(adError: LoadAdError) {
                                            activity.runOnUiThread { bannerVisible = false }
                                        }
                                    },
                                )
                            }
                        }
                    },
                    onRelease = AdView::destroy,
                )
            }
        }
    }
}
