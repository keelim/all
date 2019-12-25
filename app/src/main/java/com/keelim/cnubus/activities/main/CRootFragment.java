package com.keelim.cnubus.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.keelim.cnubus.R;
import com.keelim.cnubus.activities.MapsActivity;

public class CRootFragment extends Fragment {
    private ListView listView;
    private String[] rootList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_c_root, container, false);
        listView = root.findViewById(R.id.lv_croot);
        rootList = getResources().getStringArray(R.array.croot);
        applyList(rootList);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), rootList[position] + "정류장 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("location", "골프연습장 주차장");
            startActivity(intent);
        });
        return root;
    }

//        <string-array name="croot">
//    <item>골프연습장주차장(출발)</item>
//        <item>도서관앞</item>
//        <item>산학연구관</item>
//        <item>GS주유소</item>
//        <item>유성온천역 4번출구 옆시내버스정류장</item>
//    <item>유성온천역(7번)</item>
//        <item>정심화국제문화회관</item>
//        <item>도서관</item>
//        <item>골프연습장주차장</item>
//    </string-array>


    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }
}
