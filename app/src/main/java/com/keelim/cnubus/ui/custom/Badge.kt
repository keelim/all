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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.keelim.cnubus.utils.dip

class Badge constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var badgeColor = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val verticalPadding = dip(4f)
        val horizontalPadding = dip(8f)
        setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        setTextColor(Color.WHITE)
        textSize = 12f
        typeface = Typeface.DEFAULT_BOLD
        background = null
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            height / 2f, height / 2f,
            paint.apply { color = badgeColor }
        )

        super.onDraw(canvas)
    }
}
