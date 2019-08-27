package com.keelim.cnubus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.R;

public class EntireMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entire_map);
        ImageView imageView = findViewById(R.id.iv_entiremap);
        Glide.with(this).load("http://plus.cnu.ac.kr/images/kr/sub01/campus_map_d1.jpg").into(imageView);
    }
}
