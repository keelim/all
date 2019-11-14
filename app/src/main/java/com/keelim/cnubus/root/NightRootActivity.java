package com.keelim.cnubus.root;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.keelim.cnubus.R;

public class NightRootActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_root);
        Toast.makeText(this, "야간 노선 입니다. ", Toast.LENGTH_SHORT).show();
    }
}
