package com.keelim.cnubus.ui.content

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.R

class ScreenSliderPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private val images = arrayOf(
        R.drawable.content1,
        R.drawable.content2
    )

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment =
        ImageSlideFragment.getInstance(images[position])
}