package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.keelim.nandadiagnosis.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient()); // 클릭시 새창이 뜨지 않는다.?
        webview.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 알림 뜨지 않음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webview.setForceDarkAllowed(true);
        }
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        WebSettings settings = webview.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        String url = urlHandling();

        if (url != null) {
            webview.loadUrl(url);
        } else {
            webview.loadUrl("https://www.google.com");
            Toast.makeText(this, "페이지 오류가 있습니다. 데이터를 수집 합니다.", Toast.LENGTH_SHORT).show();
            Log.e("Error", url);
        }

    }

    private String urlHandling() {
        Intent intent = getIntent();
        if (intent.getStringExtra("URL") != null)
            return getIntent().getStringExtra("URL");
        else {
            String domain = intent.getStringExtra("url_sub"); // dommain 이름을 가지고 온다.
            switch (domain) {
                case "1":
                    return getString(R.string.url1);
                case "2":
                    return getString(R.string.url2);
                case "3":
                    return getString(R.string.url3);
                case "4":
                    return getString(R.string.url4);
                case "5":
                    return getString(R.string.url5);
                case "6":
                    return getString(R.string.url6);
                case "7":
                    return getString(R.string.url7);
                case "8":
                    return getString(R.string.url8);
                case "9":
                    return getString(R.string.url9);
                case "10":
                    return getString(R.string.url10);
                case "11":
                    return getString(R.string.url11);
                case "12":
                    return getString(R.string.url12);
                case "13":
                    return getString(R.string.url13);
            }
        }
        return "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
