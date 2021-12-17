package com.keelim.cnubus.data.model

import com.google.android.gms.maps.model.LatLng

data class Location(
    val latLng: LatLng,
    val roota:Int,
    val rootb:Int,
    val name:String,
    val rootc:Int = -1
)
