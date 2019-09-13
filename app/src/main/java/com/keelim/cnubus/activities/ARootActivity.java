package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.R;
import com.keelim.cnubus.databinding.ActivityArootBinding;

public class ARootActivity extends AppCompatActivity {
    ActivityArootBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_aroot);
        binding.setActivity(this);
        Glide.with(this).load("https://raw.githubusercontent.com/keelim/Keelim.github.io/master/assets/a.png").into(binding.ivAroot);
    }
}
