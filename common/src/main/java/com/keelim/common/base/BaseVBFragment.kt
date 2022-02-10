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
package com.keelim.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.keelim.common.extensions.allDisableChildEnable
import com.keelim.common.extensions.toggleVisibility

abstract class BaseVBFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment() {
    /* Rx 사용시
    * private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
    * */

    private var activity: BaseActivity<*, *>? = null
    private var _binding: T? = null
    private val binding get() = _binding!!

    var progressView: View? = null

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initBeforeBinding() // 뷰나 액티비티의 속성 등을 초기화.
    abstract fun initBinding() // 바인딩 설정
    abstract fun initAfterBinding() // 리스너 설정
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBeforeBinding()
        initBinding()
        initAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setProgressVisible(visible: Boolean) {
        (binding.root as ViewGroup).allDisableChildEnable(visible)
        progressView?.let {
            it.isEnabled = visible
            it.toggleVisibility()
        }
    }
}
