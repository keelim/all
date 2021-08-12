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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentContent2Binding

class Content2Fragment : Fragment() {
    private lateinit var binding: FragmentContent2Binding
    private val viewModel by viewModels<Content2ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content2, container, false)
        binding.apply {
            vm = viewModel
            lifecycleOwner = this@Content2Fragment
            pager.adapter = ScreenSliderPagerAdapter(this@Content2Fragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewEvent.observe(viewLifecycleOwner) {
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

    inner class ScreenSliderPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = PAGE_NUM

        override fun createFragment(position: Int): Fragment {
            return ImageSlideFragment(images[position])
        }
    }

    companion object {
        const val PAGE_NUM = 2
    }
}
