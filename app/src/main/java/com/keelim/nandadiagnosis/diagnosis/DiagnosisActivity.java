package com.keelim.nandadiagnosis.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.WebViewActivity;

import java.util.ArrayList;

public class DiagnosisActivity extends AppCompatActivity {
    private ArrayList<DiagnosisItem> arrayList;
    private MyDiagnosisViewAdapter adapter;
    private String pointer;
    private int nav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosislist);
        arrayList = new ArrayList<>();
        arrayListSetting();

        adapter = new MyDiagnosisViewAdapter(this, arrayList);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        Intent intent_web = new Intent(this, WebViewActivity.class);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            goWeb(nav + position + 1);
        });
    }

    private void goWeb(int total) {
        Intent intent_web = new Intent(this, WebViewActivity.class);
        intent_web.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/" + total + ".html");
        startActivity(intent_web);
    }

    private void arrayListSetting() {
        String[] array1 = getResources().getStringArray(R.array.diagnosis1);
        String[] array2 = getResources().getStringArray(R.array.test2);
        pointer = getIntent().getStringExtra("extra");
        switch (pointer) {
            case "1":
                customAdd(0, 14, array1);
                nav = 0;
                break;
            case "2":
                customAdd(15, 35, array1);
                nav = 15;
                break;
            case "3":
                customAdd(36, 54, array1);
                nav = 36;
                break;
            case "4":
                customAdd(55, 89, array1);
                nav = 55;
                break;
            case "5":
                customAdd(90, 101, array1);
                nav = 90;
                break;
            case "6":
                customAdd(102, 112, array1);
                nav = 102;
                break;
            case "7":
                customAdd(113, 127, array1);
                nav = 113;
                break;
            case "8":
                customAdd(128, 133, array1);
                nav = 128;
                break;
            case "9":
                customAdd(134, 170, array1);
                nav = 134;
                break;
            case "10":
                customAdd(171, 182, array1);
                nav = 171;
                break;
            case "11":
                customAdd(183, 224, array1);
                nav = 183;
                break;
            case "12":
                customAdd(225, 233, array1);
                nav = 225;
                break;
            case "13":
                customAdd(234, 236, array1);
                nav = 234;
                break;
        }


    }

    private void customAdd(int startPoint, int finalPoint, String[] array1) {
        for (int i = startPoint; i <= finalPoint; i++) {
            arrayList.add(new DiagnosisItem(array1[i], ""));
        }
    }

}
