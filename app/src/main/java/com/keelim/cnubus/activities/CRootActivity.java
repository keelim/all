package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.keelim.cnubus.R;
import com.keelim.cnubus.databinding.ActivityCrootBinding;

public class CRootActivity extends AppCompatActivity {
    ActivityCrootBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_croot);
        binding.setActivity(this);
    }
}
