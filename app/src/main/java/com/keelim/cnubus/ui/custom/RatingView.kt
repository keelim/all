package com.keelim.cnubus.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.CustomRatingViewBinding

class RatingView: LinearLayout {
    private var mSelected = 0
    private lateinit var binding: CustomRatingViewBinding

    constructor(context: Context) : super(context) {
        initializeViews(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeViews(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initializeViews(context, attrs)
    }

    /**
     * 레이아웃 초기화
     *
     * @param context
     */
    private fun initializeViews(context: Context, attrs: AttributeSet?) {
        binding = CustomRatingViewBinding.inflate(LayoutInflater.from(context), this)
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다
            val a = context.obtainStyledAttributes(attrs, R.styleable.RatingView)
            mSelected = a.getInteger(0, 0)
            a.recycle() // 이용이 끝났으면 recycle() 호출
        }
    }

    // inflate가 완료되는 시점에 호출된다.
    override fun onFinishInflate() {
        super.onFinishInflate()
        // 처음에만 xml로부터의 지정을 반영시키고자 두 번째 인수인 force를 true로 한다
        setSelected(mSelected, true)
    }

    fun setSelected(select: Int, force: Boolean) {
        if (force || mSelected != select) {
            if (2 > mSelected && mSelected < 0) {
                return
            }
            mSelected = select
            when (mSelected) {
                0 -> {
                    binding.star1.setImageResource(R.drawable.ic_star)
                    binding.star2.setImageResource(R.drawable.ic_star_empty)
                    binding.star3.setImageResource(R.drawable.ic_star_empty)
                }
                1 -> {
                    binding.star1.setImageResource(R.drawable.ic_star_empty)
                    binding.star2.setImageResource(R.drawable.ic_star)
                    binding.star3.setImageResource(R.drawable.ic_star_empty)
                }
                2 -> {
                    binding.star1.setImageResource(R.drawable.ic_star_empty)
                    binding.star2.setImageResource(R.drawable.ic_star_empty)
                    binding.star3.setImageResource(R.drawable.ic_star)
                }
            }
        }
    }

    /**
     * 지정된 번호로 선택한다
     *
     * @param select 지정할 번호(0이 가장 왼쪽)
     */
    var selected: Int = mSelected

    fun upTheRating(){
        selected += 1
        if (selected >= 3){
            selected = 0
        }
    }
}