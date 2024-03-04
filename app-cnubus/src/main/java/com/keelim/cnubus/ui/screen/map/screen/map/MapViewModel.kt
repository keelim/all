package com.keelim.cnubus.ui.screen.map.screen.map

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.keelim.core.data.model.locationsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
data class MapState(
    val name: String,
    val latlng: LatLng,
    val itemSnippet: String,
    val imageUrl: String,
) : ClusterItem {
    override fun getPosition(): LatLng =
        latlng

    override fun getTitle(): String =
        name

    override fun getSnippet(): String =
        itemSnippet

    override fun getZIndex(): Float? = null
}

@Stable
@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    val locations: StateFlow<PersistentList<MapState>> = locationsFlow
        .mapLatest {
            it.map { location ->
                MapState(
                    name = location.name,
                    latlng = location.latLng,
                    itemSnippet = location.name,
                    imageUrl = location.imgUrl,
                )
            }.toPersistentList()
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())
}
