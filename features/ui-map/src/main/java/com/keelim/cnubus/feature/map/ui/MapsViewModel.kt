package com.keelim.cnubus.feature.map.ui

import androidx.lifecycle.ViewModel
import com.keelim.cnubus.data.model.gps.locationList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
) : ViewModel(){
    val data =  locationList
}