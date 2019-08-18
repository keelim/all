package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.R;

public class CRootActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croot);
        Glide.with(this).load("https://raw.githubusercontent.com/keelim/Keelim.github.io/master/assets/a.png").into(imageView);
    }
}
