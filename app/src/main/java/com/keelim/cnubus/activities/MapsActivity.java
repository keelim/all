package com.keelim.cnubus.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keelim.cnubus.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        checkPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        // 구글에서 등록한 api와 엮어주기
        // 시작위치를 서울 시청으로 변경
        LatLng cityHall = new LatLng(37.566622, 126.978159); // 서울시청 위도와 경도
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cityHall));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // 시작시 마커 생성하기

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(cityHall);
        markerOptions.title("시청");
        markerOptions.snippet("서울 시청");
        // 생성된 마커 옵션을 지도에 표시
        googleMap.addMarker(markerOptions);

        // 서울광장마커
        // 회사 DB에 데이터를 가지고 있어야 된다.
        LatLng plaza = new LatLng(37.565785, 126.978056);
        markerOptions.position(plaza);
        markerOptions.title("광장");
        markerOptions.snippet("서울 광장");
        googleMap.addMarker(markerOptions);

        //맵 로드 된 이후
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override

            public void onMapLoaded() {
                Toast.makeText(MapsActivity.this, "Map로딩성공", Toast.LENGTH_SHORT).show();
            }
        });

        //카메라 이동 시작

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Log.d("set>>", "start");
            }
        });

        // 카메라 이동 중

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.d("set>>", "move");
            }
        });

        // 지도를 클릭하면 호출되는 이벤트

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 기존 마커 정리
                googleMap.clear();
                // 클릭한 위치로 지도 이동하기
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // 신규 마커 추가
                MarkerOptions newMarker = new MarkerOptions();
                newMarker.position(latLng);
                googleMap.addMarker(newMarker);
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        String[] permissions = {
                // Manifest는 android를 import
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };


        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (String permission : permissions) {
            permissionCheck = this.checkSelfPermission(permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            this.requestPermissions(permissions, 1);
        }
    }
}
