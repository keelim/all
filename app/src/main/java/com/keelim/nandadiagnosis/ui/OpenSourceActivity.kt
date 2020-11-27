package com.keelim.nandadiagnosis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivityOpenSourceBinding


class OpenSourceActivity : AppCompatActivity(R.layout.activity_open_source) {
    private lateinit var binding: ActivityOpenSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        binding.toolbarLayout.title = title
    }
}