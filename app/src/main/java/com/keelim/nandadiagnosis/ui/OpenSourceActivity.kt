package com.keelim.nandadiagnosis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.databinding.ActivityOpenSourceBinding
import kotlinx.android.synthetic.main.activity_open_source.view.*

class OpenSourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(view.toolbar)
        view.toolbar_layout.title = title
    }
}