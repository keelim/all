package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.keelim.nandadiagnosis.DomainValue;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.databinding.ActivityDomainBinding;
import com.keelim.nandadiagnosis.ui.domain.DomainPageAdapter;

public class DomainActivity extends AppCompatActivity {
    ActivityDomainBinding binding;
    private int handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_domain);
        binding.setActivity(this);

        //page adapter apply
        DomainPageAdapter domainPageAdapter = new DomainPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.domainViewPager.setAdapter(domainPageAdapter);
        binding.tabs.setupWithViewPager(binding.domainViewPager);

        //floating button
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "여기에 뭐 어떻게 할까요?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent common = getIntent();
        handler = common.getExtras().getInt("domain");
        handling(handler);
    }

    public void handling(int handler) {
        switch (handler) {
            case DomainValue.domain_hp:
                break;
            case DomainValue.domain_nt:
                break;
            case DomainValue.domain_ee:
                break;
            case DomainValue.domain_ar:
                break;
            case DomainValue.domain_pc:
                break;
            case DomainValue.domain_sp:
                break;
            case DomainValue.domain_rr:
                break;
            case DomainValue.domain_s:
                break;
            case DomainValue.domain_cs:
                break;
            case DomainValue.domain_lp:
                break;
            case DomainValue.domain_sap:
                break;
            case DomainValue.domain_c:
                break;

        }
    }
}