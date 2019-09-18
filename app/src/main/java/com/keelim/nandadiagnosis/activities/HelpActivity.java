package com.keelim.nandadiagnosis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.ui.help.HelpListAdapter;
import com.keelim.nandadiagnosis.ui.help.HelpListItem;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
    ListView helplistview;
    HelpListAdapter helpAdapter;
    ArrayList<HelpListItem> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helplistview = findViewById(R.id.help_list);

        arrayList = new ArrayList<>();
        arrayList.add(new HelpListItem("도움말"));
        arrayList.add(new HelpListItem("문의사항"));
        arrayList.add(new HelpListItem("오픈소스라이선스"));

        helpAdapter = new HelpListAdapter(getApplicationContext(), arrayList);
        helplistview.setAdapter(helpAdapter);
    }
}
