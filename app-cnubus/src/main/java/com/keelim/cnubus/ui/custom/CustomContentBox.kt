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
package com.keelim.cnubus.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.withStyledAttributes
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.CustomContentBoxBinding

class CustomContentBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = CustomContentBoxBinding.inflate(LayoutInflater.from(context), this)
    init {
        with(binding) {
            context.withStyledAttributes(attrs, R.styleable.ContentBox) {
                ivContentPicture.setImageResource(getResourceId(R.styleable.ContentBox_picture, R.drawable.content1))
                tvContentText.text = getString(R.styleable.ContentBox_contents)
            }
        }
    }

    fun setImageResource(@DrawableRes resource: Int) {
        binding.ivContentPicture.setImageResource(resource)
    }

    fun setText(message: String) {
        binding.tvContentText.text = message
    }

    fun setText(@StringRes message: Int) {
        binding.tvContentText.text = context.getString(message)
    }
}
