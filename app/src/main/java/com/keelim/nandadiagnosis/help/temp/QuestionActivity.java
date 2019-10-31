package com.keelim.nandadiagnosis.help.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.keelim.nandadiagnosis.R;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toast.makeText(this, "문의 사항 메뉴 입니다.", Toast.LENGTH_SHORT).show();
    }
    
}
