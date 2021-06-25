package com.keelim.comssa.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.usecase.SearchUseCase
import com.keelim.comssa.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
):ViewModel() {
    private val _mainListState = MutableLiveData<MainListState>(MainListState.UnInitialized)
    val mainListState: LiveData<MainListState> get() = _mainListState

    fun fetchData() = viewModelScope.launch {
        setState(
            MainListState.Loading
        )

        setState(
            MainListState.Success(
                listOf()
            )
        )
    }

    private fun setState(state:MainListState){
        _mainListState.postValue(state)
    }

    fun search(keyword:String?) = viewModelScope.launch {
        setState(
            MainListState.Loading
        )

        setState(
            MainListState.Success(
                searchUseCase.invoke(keyword.orEmpty())
            )
        )
    }

    fun favorite(favorite:Int, id:Int) = viewModelScope.launch{
        when(favorite){
            0-> updateFavoriteUseCase.invoke(1, id)
            1 -> updateFavoriteUseCase.invoke(0, id)
        }
    }
}