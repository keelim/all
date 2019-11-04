package com.keelim.nandadiagnosis.help.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.keelim.nandadiagnosis.R;

public class OpenSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);

        Toast.makeText(this, "오픈소스 라이선스 메뉴 입니다.", Toast.LENGTH_SHORT).show();
    }
}
