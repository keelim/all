package com.keelim.cnubus.ui.setting.mypage

import androidx.activity.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMyPageBinding
import com.keelim.common.base.BaseVBActivity

class MyPageActivity : BaseVBActivity<ActivityMyPageBinding, MyPageViewModel>(
    ActivityMyPageBinding::inflate
) {
    override val layoutResourceId: Int = R.layout.activity_my_page

    override val viewModel: MyPageViewModel by viewModels()

    override fun initBeforeBinding() {
        TODO("Not yet implemented")
    }

    override fun initDataBinding() {
        TODO("Not yet implemented")
    }

    override fun initAfterBinding() {
        TODO("Not yet implemented")
    }
}