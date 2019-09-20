package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keelim.nandadiagnosis.DomainValue;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        fileChecking();

    }

    private void fileChecking() {
        File check = getApplicationContext().getDatabasePath("temp.db");
        if (!check.exists()) {
            //데이터베이스를 받아온다.
            //sqlite database 파일ㅣ

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://github.com/keelim/hellosdsd/images.db")
                    .build();

            CallBackDownloadFile callBackDownloadFile = new CallBackDownloadFile(Environment.getDataDirectory().getAbsolutePath() + "/database", "temp.db");
            client.newCall(request).enqueue(callBackDownloadFile);

        }
    }


    private void toastSupport(String nanda_domain) {
        Toast.makeText(getApplicationContext(), nanda_domain, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_developer:
                Intent intent_developer = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent_developer);
                break;
            case R.id.drawer_help:
                Intent intent_help = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent_help);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toastMaker(View view) {
        switch (view.getId()) {
            case R.id.search_view_1:
                this.toastSupport("건강증진 Health promotion");
                domainInputStartActivity("domain", DomainValue.domain_hp);
                break;
            case R.id.search_view_2:
                this.toastSupport("영양 Nutrition");
                domainInputStartActivity("domain", DomainValue.domain_nt);
                break;
            case R.id.search_view_3:
                this.toastSupport("배설 Elimination and exchange");
                domainInputStartActivity("domain", DomainValue.domain_ee);
                break;
            case R.id.search_view_4:
                this.toastSupport("활동/휴식 Activity/Rest");
                domainInputStartActivity("domain", DomainValue.domain_ar);
                break;
            case R.id.search_view_5:
                this.toastSupport("지각/인지 Perception/Cognition");
                domainInputStartActivity("domain", DomainValue.domain_pc);
                break;
            case R.id.search_view_6:
                this.toastSupport("자아인식 Self_perception");
                domainInputStartActivity("domain", DomainValue.domain_sp);
                break;
            case R.id.search_view_7:
                this.toastSupport("역할 관계 Role Relationships");
                domainInputStartActivity("domain", DomainValue.domain_rr);
                break;
            case R.id.search_view_8:
                this.toastSupport("성 Sexuality");
                domainInputStartActivity("domain", DomainValue.domain_s);
                break;
            case R.id.search_view_9:
                this.toastSupport("대응/스트레스 내성 Coping/Stress Tolerance");
                domainInputStartActivity("domain", DomainValue.domain_cs);
                break;
            case R.id.search_view_10:
                this.toastSupport("생의 원리 Life Principles");
                domainInputStartActivity("domain", DomainValue.domain_lp);
                break;
            case R.id.search_view_11:
                this.toastSupport("안정/보호 Safety/Promotion");
                domainInputStartActivity("domain", DomainValue.domain_sap);
                break;
            case R.id.search_view_12:
                this.toastSupport("안위 Comfort");
                domainInputStartActivity("domain", DomainValue.domain_c);
                break;
            default:

        }
    }

    private void domainInputStartActivity(String name, int domainValue) {
        Intent intent_common = new Intent(getApplicationContext(), DomainActivity.class);
        intent_common.putExtra(name, domainValue);
        startActivity(intent_common);
    }

    private class CallBackDownloadFile implements Callback {

        private File directory;
        private File fileToBeDownloaded;

        public CallBackDownloadFile(String directory, String fileName) {
            this.directory = new File(directory);
            this.fileToBeDownloaded = new File(this.directory.getAbsolutePath() + "/" + fileName);
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
            if (!this.directory.exists()) {
                this.directory.mkdirs();
            }

            if (this.fileToBeDownloaded.exists()) {
                this.fileToBeDownloaded.delete();
            }

            try {
                this.fileToBeDownloaded.createNewFile();
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "다운로드 파일을 생성할 수 없습니다. ", Toast.LENGTH_SHORT).show();
                });
            }

            InputStream inputStream = response.body().byteStream();
            OutputStream outputStream = new FileOutputStream(this.fileToBeDownloaded);

            final int BUFFER_SIZE = 2046;
            byte[] data = new byte[BUFFER_SIZE];

            int count;
            long total = 0;

            while ((count = inputStream.read(data)) != -1) {
                total += count;
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

    private class AsyncDownloader extends AsyncTask<Void, Long, Boolean>{
        private final String URL = "file_url";
        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient httpClient = new OkHttpClient();
//            httpClient.newCall(new Request.Builder())
            return false;
        }
    }


}
