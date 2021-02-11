package com.keelim.cnubus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.utils.BackPressCloseHandler

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPressCloseHandler = BackPressCloseHandler(this)

        binding.viewpager.adapter = ViewPager2Adapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "A노선"
                1 -> tab.text = "B노선"
                2 -> tab.text = "C노선"
                3 -> tab.text = "야간노선"
                4 -> tab.text = "설정"
            }
        }.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }
}