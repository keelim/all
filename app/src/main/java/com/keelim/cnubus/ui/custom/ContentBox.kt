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
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.keelim.cnubus.R

class ContentBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_content_box, this, false)
        addView(v)
        attrs?.let { getAttrs(it) }
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ContentBox)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val picture_resID =
            typedArray.getResourceId(R.styleable.ContentBox_picture, R.drawable.content1)
        findViewById<ImageView>(R.id.contentPicture).setImageResource(picture_resID)

        val textContent = typedArray.getString(R.styleable.ContentBox_contents)
        findViewById<TextView>(R.id.contentText).text = textContent

        typedArray.recycle()
    }

    fun setPicture(picture_resID: Int) {
        findViewById<ImageView>(R.id.contentPicture).setImageResource(picture_resID)
    }

    fun setContent(textContent: String) {
        findViewById<TextView>(R.id.contentText).text = textContent
    }
}
