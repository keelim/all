package com.keelim.cnubus.ui.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewpager.adapter = ViewPagerAdapter(this)
    }

    inner class ViewPagerAdapter(private val context: Context) : PagerAdapter() {
        // R.drawable.(사진파일이름)으로 images 배열 생성
        private val images = arrayOf(R.drawable.content1, R.drawable.content2)
        private lateinit var inflater: LayoutInflater

        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        // 각각의 item을 인스턴스 화
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.layout_slider, container, false)
            val imageView = view.findViewById<View>(R.id.slider_iv) as ImageView

            Glide.with(this@ContentActivity).load(images[position]).into(imageView)
            // 아웃 오브 메모리 방지를 해준다.
//            imageView.setImageResource(images[position])
            container.addView(view)
            return view
        }
    }
}

