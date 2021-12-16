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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentImageSliderBinding

class ImageSlideFragment() : Fragment() {
    private val image: Int? by lazy { arguments?.getInt("image") }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentImageSliderBinding =
            DataBindingUtil.inflate<FragmentImageSliderBinding?>(
                inflater,
                R.layout.fragment_image_slider,
                container,
                false
            ).apply {
                image?.let {
                    imgSlideImage.load(it)
                }
            }
        return binding.root
    }

    companion object {
        fun getInstance(@DrawableRes image: Int): ImageSlideFragment {
            return ImageSlideFragment().apply {
                arguments = bundleOf(
                    "image" to image
                )
            }
        }
    }
}
