package com.keelim.comssa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.FeaturedData
import com.keelim.comssa.usecase.GetAllDatasUseCase
import com.keelim.comssa.usecase.GetRandomFeatureDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRandomFeatureDataUseCase: GetRandomFeatureDataUseCase,
    private val getAllDatasUseCase: GetAllDatasUseCase,
): ViewModel() {
    private val _randomData = MutableLiveData<FeaturedData>()
    val randomData: LiveData<FeaturedData> get() = _randomData

    private val _allData =  MutableLiveData<List<Data>>(listOf())
    val allData: LiveData<List<Data>> get() = _allData

    fun fetchData() = viewModelScope.launch{
        _randomData.value = getRandomFeatureDataUseCase.invoke()
        _allData.value = getAllDatasUseCase.invoke()
    }
}