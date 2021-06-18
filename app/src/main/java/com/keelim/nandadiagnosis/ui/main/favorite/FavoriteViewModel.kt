package com.keelim.nandadiagnosis.ui.main.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.base.BaseViewModel
import com.keelim.nandadiagnosis.ui.list.NandaListState
import com.keelim.nandadiagnosis.usecase.GetFavoriteListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FavoriteViewModel
@Inject
constructor(
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
):BaseViewModel() {
    private var _favoriteState = MutableLiveData<FavoriteListState>()
    val favoriteState: LiveData<FavoriteListState> get() = _favoriteState

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            FavoriteListState.Loading
        )
        setState(
            FavoriteListState.Success(
                getFavoriteListUseCase()
            )
        )
    }

    private fun setState(state:FavoriteListState){
        _favoriteState.postValue(state)
    }
}