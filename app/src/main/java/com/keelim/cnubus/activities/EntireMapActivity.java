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
        Glide.with(this).load("https://raw.githubusercontent.com/keelim/Keelim.github.io/master/assets/entire.jpg").into(imageView);
    }
}
