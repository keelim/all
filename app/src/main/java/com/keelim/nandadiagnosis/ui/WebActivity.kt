package com.keelim.nandadiagnosis.ui

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.databinding.ActivityWebBinding



class WebActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityWebBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.webView.apply {
            webViewClient = WebViewClient()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                isForceDarkAllowed = true //다크 모드 강제를 하는 것
                webChromeClient = WebChromeClient() //웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 알림 뜨지 않음
            }
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

    private fun urlHandling(): String? {
        return intent.getStringExtra("URL")
    }
}