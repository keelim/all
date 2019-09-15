package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
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
            case R.id.drawer_developer:
                Intent intent_developer = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent_developer);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toastMaker(View view) {
        switch (view.getId()) {
            case R.id.search_view_1:
                this.toastSupport("건강증진 Health promotion");
                break;
            case R.id.search_view_2:
                this.toastSupport("영양 Nutrition");
                break;
            case R.id.search_view_3:
                this.toastSupport("배설 Elimination and exchange");
                break;
            case R.id.search_view_4:
                this.toastSupport("활동/휴식 Activity/Rest");
                break;
            case R.id.search_view_5:
                this.toastSupport("지각/인지 Perception/Cognition");
                break;
            case R.id.search_view_6:
                this.toastSupport("자아인식 Self_perception");
                break;
            case R.id.search_view_7:
                this.toastSupport("역할 관계 Role Relationships");
                break;
            case R.id.search_view_8:
                this.toastSupport("성 Sexuality");
                break;
            case R.id.search_view_9:
                this.toastSupport("대응/스트레스 내성 Coping/Stress Tolerance");
                break;
            case R.id.search_view_10:
                this.toastSupport("생의 원리 Life Principles");
                break;
            case R.id.search_view_11:
                this.toastSupport("안정/보호 Safety/Promotion");
                break;
            case R.id.search_view_12:
                this.toastSupport("안위 Comfort");
                break;
            default:

        }
    }

    private void toastSupport(String nanda_domain) {
        Toast.makeText(getApplicationContext(), nanda_domain, Toast.LENGTH_SHORT).show();
    }
}
