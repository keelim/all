package com.keelim.nandadiagnosis.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.base.BaseViewModel
import com.keelim.nandadiagnosis.usecase.GetNandaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListFragmentViewModel
@Inject
constructor(
    private val getNandaListUseCase: GetNandaListUseCase,
) : BaseViewModel() {
    private var _nandaListState = MutableLiveData<NandaListState>(NandaListState.UnInitialized)
    val nandaListState: LiveData<NandaListState> get() = _nandaListState

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            NandaListState.Loading
        )
        setState(
            NandaListState.Success(
                getNandaListUseCase()
            )
        )
    }

    private fun setState(state:NandaListState){
        _nandaListState.postValue(state)
    }
}