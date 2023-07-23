/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.screen.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentTabBinding
import com.keelim.cnubus.ui.screen.root.RootFragment
import com.keelim.cnubus.ui.screen.setting.SettingFragment2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFragment : Fragment(R.layout.fragment_tab) {
    private lateinit var binding: FragmentTabBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabBinding.bind(view)
        initViews()
    }

    private fun initViews() = with(binding) {
        viewpager.apply {
            adapter = ViewPager2Adapter(this@TabFragment)
        }
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "A노선"
                1 -> tab.text = "B노선"
                2 -> tab.text = "C노선"
                3 -> tab.text = "야간노선"
                4 -> tab.text = "설정"
            }
        }.attach()
        animationView.playAnimation()
        animationView.repeatCount = LottieDrawable.INFINITE
    }
}

class ViewPager2Adapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private var fragments = listOf(
        RootFragment.newInstance("a"),
        RootFragment.newInstance("b"),
        RootFragment.newInstance("c"),
        RootFragment.newInstance("night"),
        SettingFragment2(),
    )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
