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
    private String pointer;


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
        pointer = getIntent().getStringExtra("extra");

    }

    private void arrayListSetting() {
        String[] array1 = getResources().getStringArray(R.array.diagnosis1);
        String[] array2 = getResources().getStringArray(R.array.test2);
        pointer = getIntent().getStringExtra("extra");
        switch (pointer) {
            case "1":
                customAdd(0, 14, array1);
                break;
            case "2":
                customAdd(15, 35, array1);
                break;
            case "3":
                customAdd(36, 54, array1);
                break;
            case "4":
                customAdd(55, 89, array1);
                break;
            case "5":
                customAdd(90, 101, array1);
                break;
            case "6":
                customAdd(102, 112, array1);
                break;
            case "7":
                customAdd(113, 127, array1);
                break;
            case "8":
                customAdd(128, 133, array1);
                break;
            case "9":
                customAdd(134, 170, array1);
                break;
            case "10":
                customAdd(171, 182, array1);
                break;
            case "11":
                customAdd(183, 224, array1);
                break;
            case "12":
                customAdd(225, 233, array1);
                break;
            case "13":
                customAdd(234, 236, array1);
                break;
        }


    }

    private void customAdd(int startPoint, int finalPoint, String[] array1) {
        for (int i = startPoint; i <= finalPoint; i++) {
            arrayList.add(new DiagnosisItem(array1[i], ""));
        }
    }

}
