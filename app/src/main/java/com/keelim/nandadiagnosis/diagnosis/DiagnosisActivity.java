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
    private int clickhandler;


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
        int handler = Integer.parseInt(pointer);
        String[] array1;
        switch (handler){
            case 1:
                array1 = getResources().getStringArray(R.array.diagnosis1);
                clickhandler=1;
                break;
            case 2:
                array1 = getResources().getStringArray(R.array.diagnosis2);
                clickhandler=2;
                break;
            case 3:
                array1 = getResources().getStringArray(R.array.diagnosis3);
                clickhandler=3;
                break;
            case 4:
                array1 = getResources().getStringArray(R.array.diagnosis4);
                clickhandler=4;
                break;
            case 5:
                array1 = getResources().getStringArray(R.array.diagnosis5);
                clickhandler=5;
                break;
            case 6:
                array1 = getResources().getStringArray(R.array.diahnosis6);
                clickhandler=6;
                break;
            case 7:
                array1 = getResources().getStringArray(R.array.diagnosis7);
                clickhandler=7;
                break;
            case 8:
                array1 = getResources().getStringArray(R.array.diagnosis8);
                clickhandler=8;
                break;
            case 9:
                array1 = getResources().getStringArray(R.array.diagnosis9);
                clickhandler=9;
                break;
            case 10:
                array1 = getResources().getStringArray(R.array.diagnosis10);
                clickhandler=10;
                break;
            case 11:
                array1 = getResources().getStringArray(R.array.diagnosis11);
                clickhandler=11;
                break;
            case 12:
                array1 = getResources().getStringArray(R.array.diagnosis12);
                clickhandler=12;
                break;
            case 13:
                array1 = getResources().getStringArray(R.array.diagnosis13);
                clickhandler=13;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + handler);
        }

        String[] array2 = getResources().getStringArray(R.array.test2);
        for (int i = 0; i < array1.length; i++) {
            arrayList.add(new DiagnosisItem(array1[i], ""));
        }
    }


}
