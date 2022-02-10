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

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.keelim.common.extensions.allDisableChildEnable
import com.keelim.common.extensions.toggleVisibility
import com.keelim.common.extensions.transparentStatusBar

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {
    /* Rx 사용시
    * private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    * */
    var transparentPoint = 0

    lateinit var binding: T

    var progressView: View? = null

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initBeforeBinding() // 뷰나 액티비티의 속성 등을 초기화.
    abstract fun initDataBinding() // 바인딩 설정
    abstract fun initAfterBinding() // 리스너 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        transparentStatusBar()
        initBeforeBinding()
        initDataBinding()
        initAfterBinding()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // EditText 가 아닌 곳을 터치하면 키보드 내림
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val (xPoint, yPoint) = ev.x.toInt() to ev.y.toInt()
            if (!rect.contains(xPoint, yPoint)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setProgressVisible(visible: Boolean) {
        (binding.root as ViewGroup).allDisableChildEnable(visible)
        progressView?.let {
            it.isEnabled = visible
            it.toggleVisibility()
        }
    }
}
