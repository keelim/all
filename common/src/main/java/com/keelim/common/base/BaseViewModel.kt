package com.keelim.common.base


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    // 현재 ViewModel에서 진행하고 있는 작업이 있는지에 대한 여부
    private val _shouldUIinProgress = MutableStateFlow(false)
    val shouldUIinProgress: StateFlow<Boolean>
        get() = _shouldUIinProgress

    private val registeredWorks = mutableMapOf<Long, Boolean>()

    fun createUIProgressWork(): Long {
        val key = System.currentTimeMillis()
        registeredWorks[key] = true
        syncProgress()
        return key
    }

    fun reportProgressIsDone(key: Long) {
        //해당 작업이 완료되었음을 보고함 -> LiveData 업데이트
        registeredWorks[key] = false
        syncProgress()
    }

    private fun syncProgress() = viewModelScope.launch {
        registeredWorks.values.removeAll { !it }
        _shouldUIinProgress.emit(registeredWorks.isNotEmpty())
    }
}