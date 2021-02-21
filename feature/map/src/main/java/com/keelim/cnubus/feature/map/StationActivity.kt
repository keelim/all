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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.keelim.cnubus.feature.map.databinding.ActivityStationBinding
import org.koin.android.ext.android.inject
import kotlin.collections.ArrayList

class StationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStationBinding
    private lateinit var locationList: ArrayList<LatLng>
    private val stationList: ArrayList<LatLng> by inject()
    private var location = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intentControl()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 구글 맵은 처음 사용을 하는 거니까
        for (index in 0..14) {
            val markerOptions = MarkerOptions()
            markerOptions.position(locationList[index]).title(markerHandling(index))
            googleMap.addMarker(markerOptions)
        }

        val cameraUpdate: CameraUpdate = if (location == -1) {
            CameraUpdateFactory.newLatLngZoom(locationList[0], 17f)
        } else {
            CameraUpdateFactory.newLatLngZoom(locationList[location], 17f)
        }
        googleMap.animateCamera(cameraUpdate)
    }

    private fun markerHandling(i: Int): String? {
        val mapArray = resources.getStringArray(R.array.stations)
        return mapArray[i]
    }

    private fun intentControl() {
        val intent = intent
        val stringLocation = intent.getStringExtra("location")
        if (stringLocation != null) {
            location = stringLocation.toInt()
        } else location = -1
    }
}
