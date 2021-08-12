/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.ui.content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.R
import com.keelim.cnubus.base.BaseActivity
import com.keelim.cnubus.databinding.ActivityContent2Binding

class Content2Activity : BaseActivity() {
    private val binding: ActivityContent2Binding by binding(R.layout.activity_content2)
    private val viewModel by viewModels<Content2ViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@Content2Activity
            pager.adapter = ScreenSliderPagerAdapter(this@Content2Activity)
        }

        viewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    Content2ViewModel.VIEW_1 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.notification_uri))
                        )
                    )
                }
            }
        }
    }

    private val images = arrayOf(
        R.drawable.content1,
        R.drawable.content2
    )

    inner class ScreenSliderPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUMS_PAGE

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ImageSlideFragment(images[position])
                1 -> ImageSlideFragment(images[position])
                else -> ImageSlideFragment(images[0])
            }
        }
    }

    companion object {
        const val NUMS_PAGE = 2
    }
}
