package com.keelim.cnubus.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.databinding.ActivityLabBinding

class LabActivity() : AppCompatActivity() {
    private val binding by lazy { ActivityLabBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        root.setOnClickListener {
            binding.ratingView.upTheRating()
        }
    }
}