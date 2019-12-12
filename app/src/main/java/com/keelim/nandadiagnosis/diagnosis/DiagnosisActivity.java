package com.keelim.nandadiagnosis.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.WebViewActivity;

import java.util.ArrayList;
import java.util.Objects;

public class DiagnosisActivity extends AppCompatActivity {
    private ArrayList<DiagnosisItem> arrayList;
    private int nav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosislist);
        arrayList = new ArrayList<>();
        arrayListSetting();

        MyDiagnosisViewAdapter adapter = new MyDiagnosisViewAdapter(this, arrayList);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        Intent intent_web = new Intent(this, WebViewActivity.class);
        listView.setOnItemClickListener((parent, view, position, id) -> goWeb(nav + position + 1));
    }

    private void goWeb(int total) {
        Intent intent_web = new Intent(this, WebViewActivity.class);
        intent_web.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/" + total + ".html");

        startActivity(intent_web);
    }

    private void arrayListSetting() {
        String[] array1 = getResources().getStringArray(R.array.diagnosis1);
        String[] array2 = getResources().getStringArray(R.array.test2);
        String pointer = getIntent().getStringExtra("extra");
        switch (Objects.requireNonNull(pointer)) {
            case "1":
                customAdd(0, 11, array1);
                nav = 0;
                break;
            case "2":
                customAdd(12, 22, array1);
                nav = 12;
                break;
            case "3":
                customAdd(23, 32, array1);
                nav = 23;
                break;
            case "4":
                customAdd(33, 51, array1);
                nav = 33;
                break;
            case "5":
                customAdd(52, 86, array1);
                nav = 52;
                break;
            case "6":
                customAdd(87, 97, array1);
                nav = 87;
                break;
            case "7":
                customAdd(98, 108, array1);
                nav = 98;
                break;
            case "8":
                customAdd(109, 123, array1);
                nav = 109;
                break;
            case "9":
                customAdd(124, 129, array1);
                nav = 124;
                break;
            case "10":
                customAdd(130, 168, array1);
                nav = 130;
                break;
            case "11":
                customAdd(169, 179, array1);
                nav = 169;
                break;
            case "12":
                customAdd(180, 207, array1);
                nav = 180;
                break;
            case "13":
                customAdd(208, 236, array1);
                nav = 208;
                break;
        }


    }

    private void customAdd(int startPoint, int finalPoint, String[] array1) {
        for (int i = startPoint; i <= finalPoint; i++) {
            arrayList.add(new DiagnosisItem(array1[i], ""));
        }
    }

}
