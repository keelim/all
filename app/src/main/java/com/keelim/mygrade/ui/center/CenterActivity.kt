package com.keelim.mygrade.ui.center

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.keelim.mygrade.databinding.ActivityCenterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterActivity : AppCompatActivity() {
    private val viewModel: CenterViewModel by viewModels()
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