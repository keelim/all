/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.cnubus.data.model.gps.LocationList
import com.keelim.cnubus.data.model.gps.locationList
import com.keelim.cnubus.feature.map.databinding.ActivityMapsBinding
import com.keelim.common.toast
import dagger.hilt.android.AndroidEntryPoint

import timber.log.Timber

@AndroidEntryPoint
class MapsActivity2 : AppCompatActivity() {
    private var location = 0

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }

    val requestLocation1 = registerForActivityResult(ActivityResultContracts.RequestPermission(), ACCESS_FINE_LOCATION) { isGranted ->
        if(isGranted.not()){
            toast("권한이 없어 종료합니다.")
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        permissionCheck()
        intentControl()
        gpsSettings()

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

            val cameraUpdate = if (location == -1) {
                CameraUpdateFactory.newLatLngZoom(locationList[0], 17f)
            } else {
                CameraUpdateFactory.newLatLngZoom(locationList[location], 17f)
            }
            googleMap.animateCamera(cameraUpdate)
        }

        initViews()
    }

    private fun gpsSettings() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("GPS를 켜주세요")
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

    private fun initViews() = with(binding){
//        markRecyclerView.adapter = mapsAdapter.apply {
//            setHasStableIds(true)
//            submitList(locationList.map {
//                LocationList(it)
//            })
//        }
    }

    private fun permissionCheck(){
        requestLocation1.launch()
    }
}
