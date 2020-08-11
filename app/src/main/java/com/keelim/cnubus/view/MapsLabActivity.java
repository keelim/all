package com.keelim.cnubus.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keelim.cnubus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsLabActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private ArrayList<LatLng> locationList;
    private int location;

    ///////////////////
    private CameraPosition cameraPosition;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;


    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) { //저장되어 있는 상태를 복원한다.
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_labmaps);

        //////  현재 위치 정하기
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        ////
        locationListInit();
        intentControl();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null)
            Toast.makeText(this, "오류가 발생을 하였습니다.", Toast.LENGTH_SHORT).show();
        else
            mapFragment.getMapAsync(this);


        FloatingActionButton floatingActionButton = findViewById(R.id.button_floating);
        floatingActionButton.setOnClickListener(v -> {
                    showCurrentPlace();
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLikelyPlaceLatLngs[0], 17));
                }
        );
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) { //구글 맵은 처음 사용을 하는 거니까
        map = googleMap;

        for (int index = 0; index < 15; index++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(locationList.get(index))
                    .title(markerHandling(index));
            map.addMarker(markerOptions);
        }
        CameraUpdate cameraUpdate;
        if (location == -1) {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationList.get(0), 17);
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationList.get(location), 17);
        }
        map.animateCamera(cameraUpdate);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    // private method
    private String markerHandling(int i) {
        String[] mapArray = getResources().getStringArray(R.array.stations);
        return mapArray[i];
    }

    private void intentControl() {
        Intent intent = getIntent();
        String stringLocation = intent.getStringExtra("location");

        if (stringLocation != null) location = Integer.parseInt(stringLocation);
        else location = -1;
    }

    private void locationListInit() {
        locationList = new ArrayList<>();
        locationList.add(new LatLng(36.363876, 127.345119)); //정삼화
        locationList.add(new LatLng(36.367262, 127.342408)); //한누리관 뒤
        locationList.add(new LatLng(36.368622, 127.341531)); // 서문
        locationList.add(new LatLng(36.374241, 127.343924)); // 음대
        locationList.add(new LatLng(36.376406, 127.344168)); // 공동 동물
        locationList.add(new LatLng(36.372513, 127.343118)); //체육관 입구
        locationList.add(new LatLng(36.370587, 127.343520)); // 예술대학앞
        locationList.add(new LatLng(36.369522, 127.346725)); // 도서관앞
        locationList.add(new LatLng(36.369119, 127.351884)); //농업생명과학대학
        locationList.add(new LatLng(36.367465, 127.352190)); //동문
        locationList.add(new LatLng(36.372480, 127.346155)); //생활관
        locationList.add(new LatLng(36.369780, 127.346901)); //도서관앞
        locationList.add(new LatLng(36.367404, 127.345517)); //공과대학앞
        locationList.add(new LatLng(36.365505, 127.345159)); //산학협력관
        locationList.add(new LatLng(36.367564, 127.345800)); //경상대학
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() { //권한 체크 하기

        //권한 체크 생각보다 간단하네

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationPermissionGranted = true;
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            //권한을 배열을 만들어서 call 을 한다.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        locationPermissionGranted = false; //일단 권한 다시 초기화
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) return;
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showCurrentPlace() {
        if (map == null) return;

        if (locationPermissionGranted) {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult = placesClient.findCurrentPlace(request);

            placeResult.addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();

                    int count = Math.min(likelyPlaces.getPlaceLikelihoods().size(), M_MAX_ENTRIES);
                    int i = 0;
                    mLikelyPlaceNames = new String[count];
                    mLikelyPlaceAddresses = new String[count];
                    mLikelyPlaceAttributions = new List[count];
                    mLikelyPlaceLatLngs = new LatLng[count];


                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        // Build a list of likely places to show the user.
                        mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                        mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                        mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                .getAttributions();
                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                        i++;
                        if (i > (count - 1)) break;

                    }
                }
            });
        } else getLocationPermission();

    }
}
