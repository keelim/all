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
package com.keelim.cnubus.feature.map.ui

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.UrlTileProvider
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.feature.map.R
import com.keelim.cnubus.feature.map.databinding.ActivityMapsBinding
import com.keelim.cnubus.feature.map.databinding.BottomSheetBinding
import com.keelim.cnubus.feature.map.ui.map3.LocationAdapter
import com.keelim.cnubus.feature.map.ui.map3.LocationPagerAdapter
import com.keelim.cnubus.feature.map.ui.map3.detail.DetailActivity
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URL

@SuppressLint("all")
@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    companion object {
        const val NORMAL_ZOOM = 17f
        const val REQUEST_INTERVAL = 10000L
        const val REQUEST_FAST_INTERVAL = 5000L
    }

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val bottomBinding by lazy { BottomSheetBinding.bind(binding.bottom.root) }
    private val location by lazy { intent.getIntExtra("location", -1) }
    private val mode by lazy { intent.getStringExtra("mode") ?: "" }
    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val viewModel: MapsViewModel by viewModels()
    private val mapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }
    private val viewPagerAdapter by lazy {
        LocationPagerAdapter(
            clicked = {
                startActivity(
                    Intent(this@MapsActivity, DetailActivity::class.java).apply {
                        putExtra("item", it)
                    }
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
                        null
                    )
                )
            }
        )
    }
    private val recyclerAdapter by lazy {
        LocationAdapter()
    }
    private val tileProvider = object : UrlTileProvider(64, 64) {
        override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
            val url = "http://my.image.server/images/$zoom/$x/$y.png"
            return if (!checkTileExists(x, y, zoom)) {
                null
            } else try {
                URL(url)
            } catch (e: MalformedURLException) {
                throw AssertionError(e)
            }
        }

        private fun checkTileExists(x: Int, y: Int, zoom: Int): Boolean {
            val minZoom = 12
            val maxZoom = 16
            return zoom in minZoom..maxZoom
        }
    }
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var myLocationListener: LocationListener
    private lateinit var googleMap: GoogleMap
    private var current: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        setMyLocationListener()
        observeState()
        googleMapSetting()
        viewModel.loadLocation(mode)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = LocationListener {
                removeLocationListener()
            }
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
        if (::fusedLocationProvider.isInitialized.not()) {
            fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation.run {
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
            Looper.myLooper()!!
        )
    }

    private fun initViews() = with(binding) {
        bottomBinding.recyclerView.adapter = recyclerAdapter
        bottomBinding.recyclerView.itemAnimator = null
        houseViewPager.adapter = viewPagerAdapter
        houseViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedHouseModel = viewPagerAdapter.currentList[position]
                CameraUpdateFactory.newLatLngZoom(
                    selectedHouseModel.latLng,
                    NORMAL_ZOOM
                )
            }
        })
    }

    private fun googleMapSetting() = lifecycleScope.launch {
        googleMap = mapFragment.awaitMap().apply {
            with(uiSettings) {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isIndoorLevelPickerEnabled = true
                isMapToolbarEnabled = true
            }
            setOnMarkerClickListener { marker ->
                val selectedModel = viewPagerAdapter.currentList.firstOrNull {
                    it.name == marker.snippet ?: "0".toInt()
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
            addTileOverlay(
                TileOverlayOptions().tileProvider(tileProvider)
            )
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                when (state) {
                    is MapEvent.UnInitialized -> toast("초기화 중입니다.")
                    is MapEvent.Loading -> Unit
                    is MapEvent.Error -> toast(state.message)
                    is MapEvent.MigrateSuccess -> {
                        googleMap = mapFragment.awaitMap()
                        updateMarker(state.data)
                        CameraUpdateFactory.newLatLngZoom(
                            if (location == -1) {
                                state.data[0].latLng
                            } else {
                                state.data[location].latLng
                            },
                            NORMAL_ZOOM
                        ).also { cameraUpdate ->
                            googleMap.run {
                                animateCamera(cameraUpdate)
                                isMyLocationEnabled = true
                                uiSettings.isMyLocationButtonEnabled = true
                            }
                        }
                        viewPagerAdapter.submitList(state.data)
                        recyclerAdapter.submitList(state.data)
                        bottomBinding.bottomSheetTitleTextView.text = "${state.data.size} 주변 장소"
                        binding.houseViewPager.currentItem = location
                    }
                }
            }
        repeatCallDefaultOnStarted(Lifecycle.State.DESTROYED) {
            removeLocationListener()
        }
    }

    private fun updateMarker(locations: List<Location>) {
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

        if (::fusedLocationProvider.isInitialized) fusedLocationProvider.removeLocationUpdates(
            locationCallback
        )
    }
}
