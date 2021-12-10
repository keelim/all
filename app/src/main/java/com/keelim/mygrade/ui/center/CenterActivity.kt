package com.keelim.mygrade.ui.center

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.keelim.mygrade.databinding.ActivityCenterBinding

class CenterActivity : AppCompatActivity() {
    private val binding: ActivityCenterBinding by lazy {
        ActivityCenterBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}