package com.keelim.cnubus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.keelim.cnubus.R;

public class GuideActivity extends AppCompatActivity { //처음 사용자를 위한 activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }
}
