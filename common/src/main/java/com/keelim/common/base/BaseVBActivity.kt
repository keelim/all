package com.keelim.common.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.keelim.common.extensions.allDisableChildEnable
import com.keelim.common.extensions.toggleVisibility
import com.keelim.common.extensions.transparentStatusBar

abstract class BaseVBActivity<T : ViewBinding, R : BaseViewModel>(
    val bindingFactory: (LayoutInflater) -> T
) : AppCompatActivity() {

    /*  use it
    * class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate)inflate
    */

    /* Rx 사용시
    * private val compositeDisposable = CompositeDisposable()
    *
    * fun addDisposable(disposable: Disposable) {
    *    compositeDisposable.add(disposable)
    * }
    * */
    var transparentPoint = 0

    private val binding: T by lazy { bindingFactory(layoutInflater) }

    var progressView: View? = null

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initBeforeBinding() //뷰나 액티비티의 속성 등을 초기화.
    abstract fun initDataBinding() // 바인딩 설정
    abstract fun initAfterBinding() //리스너 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        transparentStatusBar()
        initBeforeBinding()
        initDataBinding()
        initAfterBinding()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //EditText 가 아닌 곳을 터치하면 키보드 내림
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