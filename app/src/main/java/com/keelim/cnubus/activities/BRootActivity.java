package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.R;

public class BRootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broot);
        ImageView imageView = findViewById(R.id.iv_broot);
        Glide.with(this).load("https://raw.githubusercontent.com/keelim/Keelim.github.io/master/assets/b.png").into(imageView);

    }
}
