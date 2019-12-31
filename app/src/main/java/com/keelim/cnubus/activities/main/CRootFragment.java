package com.keelim.cnubus.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.keelim.cnubus.R;

import org.jetbrains.annotations.NotNull;

public class CRootFragment extends Fragment {
    private ListView listView;
    private String[] rootList;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_c_root, container, false);
        listView = root.findViewById(R.id.lv_croot);
        rootList = getResources().getStringArray(R.array.croot);
        applyList(rootList);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getActivity(), rootList[position] + "기능 준비 중입니다. 잠시만 기다려 주세요", Toast.LENGTH_SHORT).show();
        });
        return root;
    }



    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }
}
