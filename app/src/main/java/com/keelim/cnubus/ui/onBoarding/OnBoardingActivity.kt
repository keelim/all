package com.keelim.cnubus.ui.onBoarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding){
        vpOnboarding.adapter = OnBoardingPagerAdapter(this@OnBoardingActivity)
        dotsIndicator.setViewPager2(binding.vpOnboarding)
    }


    private inner class OnBoardingPagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa){
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> OnBoardingOneFragment(position, "first")
                    1 -> OnBoardingOneFragment(position, "second")
                    else -> OnBoardingOneFragment(position, "third")
                }
        }
    }
}