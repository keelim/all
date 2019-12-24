package com.keelim.cnubus.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keelim.cnubus.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(this, "버스 정류장을 표시 합니다.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;
        // 서울 여의도에 대한 위치 설정
        LatLng m1 = new LatLng(36.363895, 127.345148);
        LatLng m2 = new LatLng(36.367002, 127.342782);
        LatLng m3 = new LatLng(36.368622, 127.341531);
        LatLng m4 = new LatLng(36.374241, 127.343924);
        LatLng m5 = new LatLng(36.376406, 127.344168);
        LatLng m6 = new LatLng(36.372513, 127.343118);
        LatLng m7 = new LatLng(36.370587, 127.343520);
        LatLng m8 = new LatLng(36.369522, 127.346725);
        LatLng m9 = new LatLng(36.369119, 127.351884);
        LatLng m10 = new LatLng(36.367465, 127.352190);
        LatLng m11 = new LatLng(36.372480, 127.346155);
        LatLng m12 = new LatLng(36.369780, 127.346901);
        LatLng m13 = new LatLng(36.367404, 127.345517);
        LatLng m14 = new LatLng(36.365505, 127.345159);
        LatLng m15 = new LatLng(36.367564, 127.345800);


        // 구글 맵에 표시할 마커에 대한 옵션 설정

        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(m1);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m2);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m3);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m4);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m5);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m6);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m7);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m8);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m9);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m10);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m11);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m12);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m13);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m14);
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions.position(m15);
        mMap.addMarker(makerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(m1));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }


}
