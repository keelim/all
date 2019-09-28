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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.keelim.nandadiagnosis.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webview;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview.loadUrl("");
        webview.setWebViewClient(new WebViewClient()); // 클릭시 새창이 뜨지 않는다.?
        webview.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 알림 뜨지 않음
        webview.setForceDarkAllowed(true);
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

        Intent intent = getIntent(); //인텐트를 통하여 제어하기
        String url = intent.getStringExtra("URL");
        if (url != null) {
            webview.loadUrl(url);
        } else {
            webview.loadUrl("https://www.google.com");
            Toast.makeText(this, "페이지 오류가 있습니다. 데이터를 수집 합니다.", Toast.LENGTH_SHORT).show();
            Log.e("Error", url);
        }

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
