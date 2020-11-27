package com.keelim.nandadiagnosis.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity(R.layout.activity_web) {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)

        binding.webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient() //웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 알림 뜨지 않음
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = true
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

        binding.webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
        }

        binding.webView.loadUrl(urlHandling()!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun urlHandling(): String? { //인텐트에서 받아오는 값을 처리한다.
        return intent.getStringExtra("URL")
    }
}