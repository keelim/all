package com.keelim.cnubus.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.keelim.cnubus.R;
import com.keelim.cnubus.activities.main.ViewPagerAdapter;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity extends AppCompatActivity {
    private AdView adView;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        popUp(); //팝업 실행
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.drawer_root_check) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.busurl)));
            startActivity(intent);
        } else if (id == R.id.menu_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent); //설정 창으로 이동을 한다.
        } else if(id == R.id.gps){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent); //설정 창으로 이동을 한다.MapsActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
            }
        }
    }

    // private method
    private void popUp() {
        Intent intent = new Intent(MainActivity.this, PopupActivity.class);
        startActivityForResult(intent, 1); // 액티비티 간의 정보를 교환을 할 때
    }

}
