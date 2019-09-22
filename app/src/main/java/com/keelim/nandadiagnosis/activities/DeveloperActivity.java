package com.keelim.nandadiagnosis.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.databinding.ActivityDeveloperBinding;
import com.keelim.nandadiagnosis.utils.MaterialHelper;


public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.keelim.nandadiagnosis.databinding.ActivityDeveloperBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_developer);
        binding.setActivity(this);

        MaterialHelper.with(this).init().loadAbout();
        Toast.makeText(this, "개발자 관련 화면 입니다.", Toast.LENGTH_SHORT).show();
    }
}
