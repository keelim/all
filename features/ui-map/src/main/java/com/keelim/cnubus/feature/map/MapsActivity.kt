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
package com.keelim.cnubus.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.cnubus.data.model.gps.locationList
import com.keelim.cnubus.feature.map.databinding.ActivityMapsBinding
import com.keelim.common.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var current: LatLng? = null
    private var location = 0

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                it.key == Manifest.permission.ACCESS_FINE_LOCATION ||
                    it.key == Manifest.permission.ACCESS_COARSE_LOCATION
            }
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                toast("권한이 없습니다. 확인해주세요")
            }
        }

//    private val mapsAdapter by lazy { MapsAdapter{ position ->
//        lifecycleScope.launchWhenCreated {
//            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//            val googleMap = mapFragment.awaitMap()
//            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationList[position], 17f)
//            googleMap.apply {
//                animateCamera(cameraUpdate)
//            }
//        }
//    }}

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getMyLocation()
        myPositionInit()
        intentControl()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycle.coroutineScope.launchWhenCreated {
            val googleMap = mapFragment.awaitMap()
            locationList.mapIndexed { index, latLng ->
                googleMap.addMarker {
                    position(latLng)
                    title(markerHandling(index))
                    snippet(snippetHandling(index))
                }
            }
            googleMap.setOnMarkerClickListener {
                BottomSheetDialog(
                    it.title,
                    it.snippet,
                    it.position.toString()
                ).apply {
                    show(supportFragmentManager, tag)
                }
                return@setOnMarkerClickListener false
            }

            val cameraUpdate = if (location == -1) {
                CameraUpdateFactory.newLatLngZoom(locationList[0], 17f)
            } else {
                CameraUpdateFactory.newLatLngZoom(locationList[location], 17f)
            }
            googleMap.apply {
                animateCamera(cameraUpdate)
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
        }
        initViews()
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
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

        fusedLocationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener()
    }

    private fun myPositionInit() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val location = locationResult.lastLocation

                location.run {
                    current = LatLng(latitude, longitude)
                    Timber.d("위도 $latitude 경도 $longitude")
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
    }

    private fun markerHandling(index: Int): String =
        resources.getStringArray(R.array.stations)[index]

    private fun snippetHandling(index: Int): String =
        resources.getStringArray(R.array.station_info)[index]

    private fun intentControl() {
        val stringLocation = intent.getStringExtra("location")
        location = stringLocation?.toInt() ?: -1
    }

    private fun initViews() = with(binding) {
        /*
        recyclerMaps.adapter = mapsAdapter.apply {
            submitList(locationList.map {
                LocationList(it)
            })
        }*/
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
        fusedLocationProvider.removeLocationUpdates(locationCallback)
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            removeLocationListener()
        }
    }
}
