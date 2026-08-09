package com.keelim.nandadiagnosis.wellness.ads

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.keelim.nandadiagnosis.BuildConfig

@Composable
fun RoutineAdBanner(
    canLoadAd: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isPreviewMode = LocalInspectionMode.current

    BoxWithConstraints(
        modifier = modifier.height(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (canLoadAd && !isPreviewMode) {
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
                            loadAd(
                                BannerAdRequest.Builder(
                                    if (BuildConfig.DEBUG) {
                                        BuildConfig.AD_NANDA_TEST_BANNER_ID
                                    } else {
                                        BuildConfig.AD_NANDA_BANNER_ID
                                    },
                                    adSize,
                                ).build(),
                                object : AdLoadCallback<BannerAd> {},
                            )
                        }
                    },
                    onRelease = AdView::destroy,
                )
            }
        }
    }
}
