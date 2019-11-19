package com.keelim.nandadiagnosis.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.WebViewActivity;

import java.util.ArrayList;

public class DiagnosisActivity extends AppCompatActivity {
    private ArrayList<DiagnosisItem> arrayList;
    private MyDiagnosisViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosislist);
        arrayList = new ArrayList<>();
        arrayListSetting();

        adapter = new MyDiagnosisViewAdapter(this, arrayList);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("333", 333);
                    startActivity(intent);
            }
        });
    }

    private void arrayListSetting() {
        String[] array1 = getResources().getStringArray(R.array.diagnosis);
        String[] array2 = getResources().getStringArray(R.array.test2);
        for (int i = 0; i < array1.length; i++) {
            arrayList.add(new DiagnosisItem(array1[i], ""));
        }
    }
}
