package com.keelim.nandadiagnosis.help.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.keelim.nandadiagnosis.R;

public class PleaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_please);
        Toast.makeText(this, "문의사항 메뉴 입니다.", Toast.LENGTH_SHORT).show();
    }
}
