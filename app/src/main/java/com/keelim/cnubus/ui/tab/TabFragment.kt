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
package com.keelim.cnubus.ui.tab

import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentTabBinding
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFragment : BaseFragment<FragmentTabBinding, MainViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_tab
    override val viewModel: MainViewModel by activityViewModels()
    override fun initBeforeBinding() = Unit
    override fun initBinding() {
        initViews()
    }

    override fun initAfterBinding() = Unit
    private fun initViews() = with(binding) {
        viewpager.apply {
            adapter = ViewPager2Adapter(this@TabFragment)
            setPageTransformer(DepthPageTransformer())
        }
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "A노선"
                1 -> tab.text = "B노선"
                2 -> tab.text = "C노선"
                3 -> tab.text = "야간노선"
                4 -> tab.text = "시간표"
                5 -> tab.text = "설정"
            }
        }.attach()
        animationView.playAnimation()
        animationView.repeatCount = LottieDrawable.INFINITE
    }
}
