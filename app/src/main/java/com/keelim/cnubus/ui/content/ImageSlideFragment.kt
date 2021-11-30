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
            DataBindingUtil.inflate<FragmentImageSliderBinding?>(inflater,
                R.layout.fragment_image_slider,
                container,
                false).apply {
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
