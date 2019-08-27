package com.keelim.cnubus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.keelim.cnubus.R;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_setting, R.id.navigation_aroot, R.id.navigation_broot, R.id.navigation_croot)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_aroot:
                Intent intent_aroot = new Intent(getApplicationContext(), ARootActivity.class);
                startActivity(intent_aroot);
                break;
            case R.id.drawer_broot:
                Intent intent_broot = new Intent(getApplicationContext(), BRootActivity.class);
                startActivity(intent_broot);
                break;
            case R.id.drawer_croot:
                Intent intent_croot = new Intent(getApplicationContext(), CRootActivity.class);
                startActivity(intent_croot);
                break;
            case R.id.drawer_eniremap:
                Intent intent_entiremap = new Intent(getApplicationContext(), EntireMapActivity.class);
                startActivity(intent_entiremap);
                break;
            case R.id.drawer_developer:
                Intent intent_developer = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent_developer);
                break;
        }


        return super.onOptionsItemSelected(item);


    }
}
