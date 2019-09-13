package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.DataBinderMapperImpl;
import com.keelim.cnubus.R;
import com.keelim.cnubus.databinding.ActivityNightRootBinding;

public class NightRootActivity extends AppCompatActivity {
    ActivityNightRootBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_night_root);
        binding.setActivity(this);
        Toast.makeText(this, "야간 노선 입니다. ", Toast.LENGTH_SHORT).show();
    }
}
