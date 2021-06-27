package com.keelim.comssa.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.usecase.GetFavoriteUseCase
import com.keelim.comssa.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    ) : ViewModel() {
    private val _favoriteState = MutableLiveData<FavoriteState>(FavoriteState.UnInitialized)
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState

    fun fetchData() = viewModelScope.launch {
        setState(
            FavoriteState.Loading
        )

        setState(
            FavoriteState.Success(
                getFavoriteUseCase.invoke()
            )
        )
    }
    fun favorite(favorite: Int, id: Int) = viewModelScope.launch {
        when (favorite) {
            1 -> updateFavoriteUseCase.invoke(0, id)
            0 -> updateFavoriteUseCase.invoke(1, id)
        }
    }


    private fun setState(state: FavoriteState) {
        _favoriteState.postValue(state)
    }
}