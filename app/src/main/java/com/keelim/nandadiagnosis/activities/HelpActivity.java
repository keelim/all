package com.keelim.nandadiagnosis.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.ui.help.HelpListAdapter;
import com.keelim.nandadiagnosis.ui.help.HelpListItem;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.keelim.nandadiagnosis.databinding.ActivityHelpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        binding.setActivity(this);

        ArrayList<HelpListItem> arrayList = arrayListSetting();
        HelpListAdapter helpAdapter = new HelpListAdapter(getApplicationContext(), arrayList);

        binding.helpList.setAdapter(helpAdapter);
        binding.helpList.setOnItemClickListener((adapterView, view, i, l) -> {
            //i position
            switch (i) {
                case 0:
                    Snackbar.make(view, "도움말을 누르셨습니다. ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                case 1:
                    Snackbar.make(view, "문의사항을 누르셨습니다.  ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                case 2:
                    Snackbar.make(view, "오픈소스 라이선스", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    @NonNull
    private ArrayList<HelpListItem> arrayListSetting() {
        ArrayList<HelpListItem> arrayList = new ArrayList<>();
        arrayList.add(new HelpListItem("도움말"));
        arrayList.add(new HelpListItem("문의사항"));
        arrayList.add(new HelpListItem("오픈소스라이선스"));
        return arrayList;
    }
}
