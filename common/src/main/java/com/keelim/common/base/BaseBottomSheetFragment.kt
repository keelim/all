package com.keelim.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.common.extensions.allDisableChildEnable
import com.keelim.common.extensions.toggleVisibility

abstract class BaseBottomSheetFragment<T : ViewDataBinding, R : BaseViewModel> : BottomSheetDialogFragment() {
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

    lateinit var binding: T

    var progressView: View? = null

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initBeforeBinding() //뷰나 액티비티의 속성 등을 초기화.
    abstract fun initBinding() // 바인딩 설정
    abstract fun initAfterBinding() //리스너 설정

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBeforeBinding()
        initBinding()
        initAfterBinding()
    }

    fun setProgressVisible(visible: Boolean) {
        (binding.root as ViewGroup).allDisableChildEnable(visible)
        progressView?.let {
            it.isEnabled = visible
            it.toggleVisibility()
        }
    }
}
