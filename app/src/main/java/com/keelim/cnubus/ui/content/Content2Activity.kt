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
package com.keelim.cnubus.ui.content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityContent2Binding

class Content2Activity : AppCompatActivity() {
    private val binding by lazy { ActivityContent2Binding.inflate(layoutInflater) }
    private val viewModel: Content2ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
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

    private fun initViews() = with(binding) {
        vm = viewModel
        lifecycleOwner = this@Content2Activity
        pager.adapter = ScreenSliderPagerAdapter(this@Content2Activity)
    }

    private val images = arrayOf(
        R.drawable.content1,
        R.drawable.content2
    )

    inner class ScreenSliderPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment =
            ImageSlideFragment.getInstance(images[position])
    }
}
