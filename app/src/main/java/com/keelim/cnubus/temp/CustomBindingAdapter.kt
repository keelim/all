package com.keelim.cnubus.temp

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("bind_image", "bind_image_error")
fun bindImage(view: ImageView, res:Int?, error: Drawable){
    val options = RequestOptions()
        .error(error)

    Glide.with(view.context)
        .load(res)
        .into(view)
}

@BindingAdapter("bind_image")
fun bindImage(view: ImageView, res: Int?){
    Glide.with(view.context)
        .load(res)
        .into(view)
}