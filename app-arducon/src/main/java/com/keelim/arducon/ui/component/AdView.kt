package com.keelim.arducon.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.keelim.arducon.adID

@Composable
fun AdBannerView(
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                adUnitId = adID
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}
