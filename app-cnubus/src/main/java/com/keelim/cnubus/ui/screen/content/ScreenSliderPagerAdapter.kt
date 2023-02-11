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
package com.keelim.cnubus.ui.screen.content

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
