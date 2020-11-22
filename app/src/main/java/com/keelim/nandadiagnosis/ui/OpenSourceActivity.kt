package com.keelim.nandadiagnosis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.databinding.ActivityOpenSourceBinding


class OpenSourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        binding.toolbarLayout.title = title
    }
}