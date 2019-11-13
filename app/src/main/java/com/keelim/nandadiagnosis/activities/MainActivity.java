package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.keelim.nandadiagnosis.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(nav_view, navController);
        fileChecking();
    }

    private void fileChecking() {
        File check = new File(getDataDir().getAbsolutePath() + "/databases/nanda.db");
        if (!check.exists()) {
            //데이터베이스를 받아온다.
            alertBuilderSetting();
        } else {
            Toast.makeText(this, "데이터베이스가 존재합니다. 그대로 진행 합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertBuilderSetting() { //okhttp 작동 방식은 나중에 확인을 해보자
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("다운로드 요청")
                .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    Toast.makeText(this, "서버로부터 데이터 베이스를 요청 합니다. ", Toast.LENGTH_SHORT).show();

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://github.com/keelim/Keelim.github.io/raw/master/assets/nanda.db")
                            .build();
                    CallBackDownloadFile callBackDownloadFile = new CallBackDownloadFile("nanda.db");
                    client.newCall(request).enqueue(callBackDownloadFile);
                }).create()
                .show();
    }


    private void toastSupport(String nanda_domain) { //todo 전체적인 영역 설명이 필요한 곳
        Toast.makeText(getApplicationContext(), nanda_domain, Toast.LENGTH_SHORT).show();
    }


    public void toastMaker(View view) {
        switch (view.getId()) {
            case R.id.search_view_1:
                this.toastSupport("건강증진 Health promotion");
                UrlStartActivity(getString(R.string.url1));
                break;
            case R.id.search_view_2:
                this.toastSupport("영양 nutrition");
                UrlStartActivity(getString(R.string.url2));
                break;
            case R.id.search_view_3:
                this.toastSupport("배설 elimination and exchange");
                UrlStartActivity(getString(R.string.url3));
                break;
            case R.id.search_view_4:
                this.toastSupport("활동/휴식 Activity/Rest");
                UrlStartActivity(getString(R.string.url4));
                break;
            case R.id.search_view_5:
                this.toastSupport("지각/인지 Perception/Cognition");
                UrlStartActivity(getString(R.string.url5));
                break;
            case R.id.search_view_6:
                this.toastSupport("자아인식 Self_perception");
                UrlStartActivity(getString(R.string.url6));
                break;
            case R.id.search_view_7:
                this.toastSupport("역할 관계 Role Relationships");
                UrlStartActivity(getString(R.string.url7));
                break;
            case R.id.search_view_8:
                this.toastSupport("성 sexuality");
                UrlStartActivity(getString(R.string.url8));
                break;
            case R.id.search_view_9:
                this.toastSupport("대응/스트레스 내성 Coping/Stress Tolerance");
                UrlStartActivity(getString(R.string.url9));
                break;
            case R.id.search_view_10:
                this.toastSupport("생의 원리 Life Principles");
                UrlStartActivity(getString(R.string.url10));
                break;
            case R.id.search_view_11:
                this.toastSupport("안정/보호 Safety/Promotion");
                UrlStartActivity(getString(R.string.url11));
                break;
            case R.id.search_view_12:
                this.toastSupport("안위 comfort");
                UrlStartActivity(getString(R.string.url12));
                break;
            default:

        }
    }

    private void UrlStartActivity(String domainValue) {
        Intent intent_url = new Intent(getApplicationContext(), WebViewActivity.class);
        intent_url.putExtra("URL", domainValue);
        startActivity(intent_url);
    }

    private class CallBackDownloadFile implements Callback { //okhttp call back method
        private File fileToBeDownloaded;

        CallBackDownloadFile(String fileName) {
            this.fileToBeDownloaded = new File(getDataDir().getAbsolutePath() + "/databases", fileName);
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "파일을 다운로드 할 수 없습니다. 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            try {
                boolean flag = this.fileToBeDownloaded.createNewFile();
                Log.e("file create", "파일 만들기: " + flag);
            } catch (IOException e) {

                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "다운로드 파일을 생성할 수 없습니다.\n 데이터베이스 부족으로 인해 종료 합니다. ", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
            InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
            OutputStream outputStream = new FileOutputStream(this.fileToBeDownloaded);

            final int BUFFER_SIZE = 2046;
            byte[] data = new byte[BUFFER_SIZE];

            int count;

            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "다운로드가 완료되었습니다. ", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
