package com.keelim.cnubus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.services.TerminateService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startService(Intent(this, TerminateService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, TerminateService::class.java))
    }
}