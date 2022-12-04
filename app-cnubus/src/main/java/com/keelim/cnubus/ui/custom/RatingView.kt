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
import android.widget.LinearLayout
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.CustomRatingViewBinding

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomRatingViewBinding.inflate(LayoutInflater.from(context), this)
    private var mSelected = 0 // 선택된 번호

    // 객체 초기화 될때 호출 된다.
    init {
        // attrs.xml에 정의한 스타일을 가져온다 즉 (attrs.xml에서 발생된 selected 속성이
        // 발생되어 private void setSelected(int select, boolean force)를 호출하게 된다.
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatingView)
        mSelected = a.getInteger(0, 0)
        a.recycle() // 이용이 끝났으면 recycle() 호출
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setSelectedCustom(mSelected)
    }

    /**
     * 지정된 번호로 선택한다
     *
     * @param selected 지정할 번호(0이 가장 왼쪽)
     */
    var selected: Int
        get() = mSelected
        set(select) {
            setSelectedCustom(select)
        }

    private fun setSelectedCustom(select: Int) {
        mSelected = select
        when (mSelected) {
            0 -> {
                binding.ivStart1.setImageResource(R.drawable.ic_star)
                binding.ivStart2.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart3.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart4.setImageResource(R.drawable.ic_star_empty)
            }
            1 -> {
                binding.ivStart1.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart2.setImageResource(R.drawable.ic_star)
                binding.ivStart3.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart4.setImageResource(R.drawable.ic_star_empty)
            }
            2 -> {
                binding.ivStart1.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart2.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart3.setImageResource(R.drawable.ic_star)
                binding.ivStart4.setImageResource(R.drawable.ic_star_empty)
            }
            3 -> {
                binding.ivStart1.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart2.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart3.setImageResource(R.drawable.ic_star_empty)
                binding.ivStart4.setImageResource(R.drawable.ic_star)
            }
        }
    }
}
