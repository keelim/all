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
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
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