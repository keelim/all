package com.keelim.nandadiagnosis.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.WebViewActivity;

import java.util.ArrayList;

public class DiagnosisFragment extends Fragment {
    private ArrayList<DiagnosisItem> arrayList;
    private MyDiagnosisViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diagnosis_list, container, false);
        arrayList = new ArrayList<>();
        arrayListSetting();

        adapter = new MyDiagnosisViewAdapter(getContext(), arrayList);

        ListView listView = container.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("333", 333);
                    startActivity(intent);
            }
        });

        return root;
    }

    private void arrayListSetting() {
        String[] array1 = getResources().getStringArray(R.array.test1);
        String[] array2 = getResources().getStringArray(R.array.test2);
        for (int i = 0; i < array1.length; i++) {
            arrayList.add(new DiagnosisItem(array1[i], array2[i]));
        }
    }
}
