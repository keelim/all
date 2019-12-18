package com.keelim.cnubus.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keelim.cnubus.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LocationManager lm;
    private GoogleMap mMap;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // 궎권한을 다시 한번 확인을 하는메소드
            return;
        }
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        double[] now = nowLocation();
        LatLng focus = new LatLng(now[0], now[1]);
        mMap = googleMap;

        LatLng one = new LatLng(36.363882, 127.345151);
        mMap.addMarker(new MarkerOptions().position(one).title("Marker in first"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(focus));
    }

    private double[] nowLocation() {
        double[] array = new double[2];
        array[0] = location.getLongitude();
        array[1] = location.getLatitude();
        return array;
    }
}
