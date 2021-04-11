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

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentImageSliderBinding

class ImageSlideFragment(@DrawableRes val image: Int) : Fragment(R.layout.fragment_image_slider) {
    private var _fragmentImageSliderBinding: FragmentImageSliderBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentImageSliderBinding.bind(view)
        _fragmentImageSliderBinding = binding
        binding.imgSlideImage.setImageResource(image)
    }

    override fun onDestroyView() {
        _fragmentImageSliderBinding = null
        super.onDestroyView()
    }
}
