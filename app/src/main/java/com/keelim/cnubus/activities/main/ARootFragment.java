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

import java.util.ArrayList;

public class ARootFragment extends Fragment {
    private ListView listView;
    private String[] rootList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_a_root, container, false);
        listView = root.findViewById(R.id.lv_aroot);
        rootList = getResources().getStringArray(R.array.aroot);
        applyList(rootList);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), rootList[position] + "정류장 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("location", "골프연습장 주차장");
            startActivity(intent);

        });
        return root;
    }

    private void goMap(Intent intent, String string) {
        intent.putExtra("map", string);
        startActivity(intent);
    }

    /*<item>정심화국제문화회관</item>
        <item>경상대학앞</item>
    <item>도서관 앞(농대방향)</item>
        <item>학생생활관3거리</item>
    <item>농업생명과학대학 앞</item>
        <item>동문주차장</item>
    <item>농업생명과학대학 앞</item>
    <item>도서관앞(도서관 삼거리 방향)</item>
        <item>예술대학앞</item>
        <item>음악2호관앞</item>
    <item>공동동물실험센터 입구(회차)</item>
    <item>체육관 입구</item>
    <item>서문(공동실험실습관앞)</item>
    <item>사회과학대학 입구(한누리회관뒤)</item>
        <item>산학연교육연구관앞</item>
        <item>정심화국제문화회관</item>*/

    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }
}
