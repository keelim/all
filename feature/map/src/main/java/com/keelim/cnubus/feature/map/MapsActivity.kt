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
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.keelim.cnubus.feature.map.databinding.ActivityMapsBinding
import timber.log.Timber
import java.util.ArrayList

class MapsActivity : AppCompatActivity() {
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var current: LatLng? = null
    private var locationList: ArrayList<LatLng>? = null
    private var location = 0
    private var locationPermissionGranted = false

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationListInit()
        intentControl()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycle.coroutineScope.launchWhenCreated {
            val googleMap = mapFragment.awaitMap()
            addClickListener(googleMap)

            (0..14).forEach { index ->
                googleMap.addMarker {
                    position(locationList!![index])
                    title(markerHandling(index))
                    snippet(snippetHandling(index))
                }
            }
            val cameraUpdate: CameraUpdate = if (location == -1) {
                CameraUpdateFactory.newLatLngZoom(locationList!![0], 17f)
            } else {
                CameraUpdateFactory.newLatLngZoom(locationList!![location], 17f)
            }
            googleMap.animateCamera(cameraUpdate)
            getLocationPermission()
            updateLocationUI(googleMap)
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "제대로된 작동을  위해 GPS 를 켜주세요", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    private suspend fun addClickListener(googleMap: GoogleMap) {
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

    private fun markerHandling(index: Int): String =
        resources.getStringArray(R.array.stations)[index]

    private fun snippetHandling(index: Int): String =
        resources.getStringArray(R.array.station_info)[index]

    private fun intentControl() {
        val stringLocation = intent.getStringExtra("location")
        location = stringLocation?.toInt() ?: -1
    }

    private fun locationListInit() {
        myPositionInit()

        locationList = ArrayList()
        locationList!!.add(LatLng(36.363876, 127.345119)) // 정삼화
        locationList!!.add(LatLng(36.367262, 127.342408)) // 한누리관 뒤
        locationList!!.add(LatLng(36.368622, 127.341531)) // 서문
        locationList!!.add(LatLng(36.374241, 127.343924)) // 음대
        locationList!!.add(LatLng(36.376406, 127.344168)) // 공동 동물
        locationList!!.add(LatLng(36.372513, 127.343118)) // 체육관 입구
        locationList!!.add(LatLng(36.370587, 127.343520)) // 예술대학앞
        locationList!!.add(LatLng(36.369522, 127.346725)) // 도서관앞
        locationList!!.add(LatLng(36.369119, 127.351884)) // 농업생명과학대학
        locationList!!.add(LatLng(36.367465, 127.352190)) // 동문
        locationList!!.add(LatLng(36.372480, 127.346155)) // 생활관
        locationList!!.add(LatLng(36.369780, 127.346901)) // 도서관앞
        locationList!!.add(LatLng(36.367404, 127.345517)) // 공과대학앞
        locationList!!.add(LatLng(36.365505, 127.345159)) // 산학협력관
        locationList!!.add(LatLng(36.367564, 127.345800)) // 경상대학
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(
            cancel = {
                showPermissionInfoDialog()
            },
            ok = {
                addLocationListener()
            }
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

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
    }

    private fun updateLocationUI(mMap: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings?.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Timber.e(e)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
