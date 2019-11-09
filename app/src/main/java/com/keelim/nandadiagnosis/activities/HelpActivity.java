package com.keelim.nandadiagnosis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.help.HelpListAdapter;
import com.keelim.nandadiagnosis.help.HelpListItem;
import com.keelim.nandadiagnosis.help.activity.OpenSourceActivity;
import com.keelim.nandadiagnosis.help.activity.PleaseActivity;
import com.keelim.nandadiagnosis.help.activity.QuestionActivity;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ArrayList<HelpListItem> arrayList = arrayListSetting();
        HelpListAdapter helpAdapter = new HelpListAdapter(getApplicationContext(), arrayList);

        ListView help_list = findViewById(R.id.help_list);
        help_list.setAdapter(helpAdapter);
        help_list.setOnItemClickListener((adapterView, view, i, l) -> {
            //i position
            switch (i) {
                case 0:
                    intentControl(QuestionActivity.class);
                    return;
                case 1:
                    intentControl(PleaseActivity.class);
                    return;
                case 2:
                    intentControl(OpenSourceActivity.class);
            }
        });
    }

    private void intentControl(Class classNum) {
        Intent intent_question = new Intent(getApplicationContext(), classNum);
        startActivity(intent_question);
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
