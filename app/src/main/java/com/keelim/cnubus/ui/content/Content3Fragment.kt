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
package com.keelim.cnubus.ui.content

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentContentBinding
import com.keelim.common.base.BaseFragment
import com.keelim.common.extensions.repeatCallDefaultOnStarted

class Content3Fragment : BaseFragment<FragmentContentBinding, Content3ViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_content
    override val viewModel: Content3ViewModel by viewModels()

    override fun initBeforeBinding() = with(binding) {
        vm = viewModel
        lifecycleOwner = this@Content3Fragment
        pager.adapter = ScreenSliderPagerAdapter(this@Content3Fragment)
    }

    override fun initBinding() {
        observeEvent()
    }

    override fun initAfterBinding() = Unit

    private fun observeEvent() = repeatCallDefaultOnStarted {
        viewModel.viewEvent.collect {
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
}
