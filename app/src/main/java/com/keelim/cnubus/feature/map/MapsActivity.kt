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
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.gps.locationList
import com.keelim.cnubus.databinding.ActivityMapsBinding
import com.keelim.common.toast
import timber.log.Timber

class MapsActivity : AppCompatActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var current: LatLng? = null
    private var location = 0
    private var locationPermissionGranted = false

    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requestLocationPermission()
        myPositionInit()
        intentControl()
        gpsSettings()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycle.coroutineScope.launchWhenCreated {
            val googleMap = mapFragment.awaitMap()
            addClickListener(googleMap)

            locationList.mapIndexed { index, latLng ->
                googleMap.addMarker {
                    position(latLng)
                    title(markerHandling(index))
                    snippet(snippetHandling(index))
                }
            }

            val cameraUpdate: CameraUpdate = if (location == -1) {
                CameraUpdateFactory.newLatLngZoom(locationList[0], 17f)
            } else {
                CameraUpdateFactory.newLatLngZoom(locationList[location], 17f)
            }
            googleMap.animateCamera(cameraUpdate)
            updateLocationUI(googleMap)
        }
    }

    private fun gpsSettings() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("GPS를 켜주세요")
        }
    }

    private fun addClickListener(googleMap: GoogleMap) {
        binding.floating.setOnClickListener {
            lifecycle.coroutineScope.launchWhenStarted {
                googleMap.run {
                    animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17f))
                    googleMap.addMarker {
                        position(current!!)
                        draggable(true)
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(
            cancel = { showPermissionInfoDialog() },
            ok = { addLocationListener() }
        )
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener()
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun removeLocationListener() {
        fusedLocationProvider.removeLocationUpdates(locationCallback)
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this)
            .setMessage("현재 위치 정보를 얻으려면 위치 권한이 필요합니다\n 권한이 필요한 이유")
            .setPositiveButton("yes") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MapsActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1000
                )
            }
            .create()
            .show()
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                cancel()
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1000
                )
        } else
            ok()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted =
            requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI(mMap: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                requestLocationPermission()
            }
        } catch (e: SecurityException) {
            Timber.e(e)
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

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
