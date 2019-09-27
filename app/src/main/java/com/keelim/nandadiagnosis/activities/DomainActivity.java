package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.utils.DomainValue;

import java.util.Objects;

public class DomainActivity extends AppCompatActivity { //todo 전체적으로 수정이 필요한 부분
    //그냥 웹뷰만 넣자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.keelim.nandadiagnosis.databinding.ActivityDomainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_domain);
        binding.setActivity(this);

        //floating button
        binding.fab.setOnClickListener(view -> Snackbar.make(view, "여기에 뭐 어떻게 할까요?", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        Intent common = getIntent();
        int handler = Objects.requireNonNull(common.getExtras()).getInt("domain");
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