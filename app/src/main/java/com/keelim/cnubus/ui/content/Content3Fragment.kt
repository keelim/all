package com.keelim.cnubus.ui.content

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentContentBinding
import com.keelim.common.base.BaseFragment

class Content3Fragment : BaseFragment<FragmentContentBinding, Content3ViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_content
    override val viewModel: Content3ViewModel by viewModels()

    override fun initBeforeBinding() = with(binding){
        vm = viewModel
        lifecycleOwner = this@Content3Fragment
        pager.adapter = ScreenSliderPagerAdapter(this@Content3Fragment)
    }

    override fun initBinding() {
        viewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    Content3ViewModel.VIEW_1 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.notification_uri))
                        )
                    )
                }
            }
        }
    }

    override fun initAfterBinding() = Unit
}