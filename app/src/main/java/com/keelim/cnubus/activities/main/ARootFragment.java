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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ARootFragment extends Fragment {
    private ListView listView;
    private String[] rootList;
    private String[] intentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_a_root, container, false);
        listView = root.findViewById(R.id.lv_aroot);
        rootList = getResources().getStringArray(R.array.aroot);
        intentList = getResources().getStringArray(R.array.a_intent_array);
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
