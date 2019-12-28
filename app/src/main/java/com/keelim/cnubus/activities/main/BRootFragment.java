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

public class BRootFragment extends Fragment {
    private ListView listView;
    private String[] rootList;
    private String[] intentList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_b_root, container, false);
        listView =  root.findViewById(R.id.lv_broot);
        rootList =getResources().getStringArray(R.array.broot);
        intentList = getResources().getStringArray(R.array.b_intent_array);
        applyList(rootList);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), rootList[position] + "정류장 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("location", intentList[position]);
            startActivity(intent);
        });
        return root;
    }


    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }
}
