/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.map.screen.map1

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.common.extensions.toast
import com.keelim.data.model.gps.Location
import com.keelim.map.databinding.ActivityMapsBinding
import com.keelim.map.screen.map3.LocationAdapter
import com.keelim.map.screen.map3.LocationPagerAdapter
import com.keelim.map.screen.map3.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

sealed class MapEvent {
    object UnInitialized : MapEvent()
    object Loading : MapEvent()
    data class MigrateSuccess(val data: List<Location>) : MapEvent()
    data class Error(val message: String = "에러가 발생하였습니다.") : MapEvent()
}

@SuppressLint("all")
@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val location by lazy {
        intent.getIntExtra("location", -1)
            .let { value ->
                if (value == -1) {
                    toast("알수 없는 에러가 발생했습니다.")
                    finish()
                }
                value
            }
    }
    private val mode by lazy {
        intent.getStringExtra("mode") ?: ""
            .let { value ->
                if (value.isEmpty()) {
                    toast("알수 없는 에러가 발생했습니다.")
                    finish()
                }
                value
            }
    }
    private val locationManager by lazy { getSystemService(LOCATION_SERVICE) as LocationManager }
    private val viewModel by viewModels<MapsViewModel>()
    private val mapFragment by lazy { supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment }
    private val viewPagerAdapter by lazy {
        LocationPagerAdapter(
            clicked = {
                startActivity(
                    Intent(this@MapsActivity, DetailActivity::class.java).apply {
                        putExtra("item", it)
                    },
                )
            },
            longClicked = {
                startActivity(
                    Intent.createChooser(
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "[확인] ${it.name} 사진보기 : ${it.imgUrl}")
                            type = "text/plain"
                        },
                        null,
                    ),
                )
            },
        )
    }
    private val recyclerAdapter by lazy { LocationAdapter() }

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var myLocationListener: LocationListener
    private var current: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        setMyLocationListener()
        initFlow()
        googleMapSetting()
    }

    private fun setMyLocationListener() {
        val minTime: Long = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = LocationListener {
                removeLocationListener()
            }
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                myLocationListener,
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                myLocationListener,
            )
        }
        if (::fusedLocationProvider.isInitialized.not()) {
            fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.run {
                    current = LatLng(latitude, longitude)
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = REQUEST_INTERVAL
            fastestInterval = REQUEST_FAST_INTERVAL
        }

        fusedLocationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!,
        )
    }

    private fun initViews() = with(binding) {
        with(houseViewPager) {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    CameraUpdateFactory.newLatLngZoom(
                        viewPagerAdapter.currentList[position].latLng,
                        NORMAL_ZOOM,
                    )
                }
            })
        }
    }

    private fun googleMapSetting() = lifecycleScope.launch {
        mapFragment.awaitMap()
            .apply {
                with(uiSettings) {
                    isZoomControlsEnabled = true
                    isCompassEnabled = true
                    isMyLocationButtonEnabled = true
                    isIndoorLevelPickerEnabled = true
                    isMapToolbarEnabled = true
                }
                setOnMarkerClickListener { marker ->
                    val selectedModel = viewPagerAdapter.currentList.firstOrNull {
                        it.name == (marker.snippet ?: "0".toInt())
                    }
                    selectedModel?.let {
                        with(binding) {
                            val position = viewPagerAdapter.currentList.indexOf(it)
                            houseViewPager.currentItem = position
                            tvTitle.text = it.name
                        }
                    }
                    return@setOnMarkerClickListener false
                }
            }
    }

    private fun initFlow() {
        lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    when (state) {
                        is MapEvent.UnInitialized -> toast("초기화 중입니다.")
                        is MapEvent.Loading -> Unit
                        is MapEvent.Error -> toast(state.message)
                        is MapEvent.MigrateSuccess -> {
                            mapFragment
                                .awaitMap()
                                .run {
                                    updateMarker(state.data, this)
                                    CameraUpdateFactory.newLatLngZoom(
                                        if (location == -1) {
                                            state.data[0].latLng
                                        } else {
                                            state.data[location].latLng
                                        },
                                        NORMAL_ZOOM,
                                    ).also { cameraUpdate ->
                                        animateCamera(cameraUpdate)
                                        isMyLocationEnabled = true
                                        uiSettings.isMyLocationButtonEnabled = true
                                    }
                                }
                            viewPagerAdapter.submitList(state.data)
                            recyclerAdapter.submitList(state.data)
                            binding.houseViewPager.currentItem = location
                        }
                    }
                }
        }
        repeatCallDefaultOnStarted(Lifecycle.State.DESTROYED) {
            removeLocationListener()
        }
    }

    private fun updateMarker(locations: List<Location>, googleMap: GoogleMap) {
        locations.forEach { location ->
            googleMap.addMarker {
                position(location.latLng)
                title(location.name)
                snippet(location.name)
            }
        }
    }

    private fun removeLocationListener() {
        if (::myLocationListener.isInitialized) locationManager.removeUpdates(myLocationListener)

        if (::fusedLocationProvider.isInitialized) {
            fusedLocationProvider.removeLocationUpdates(
                locationCallback,
            )
        }
    }
    companion object {
        const val NORMAL_ZOOM = 17f
        const val REQUEST_INTERVAL = 10000L
        const val REQUEST_FAST_INTERVAL = 5000L
    }
}
