package com.keelim.cnubus.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.keelim.cnubus.R;

public class NightRootFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listView = listView.findViewById(R.id.lv_nightroot);
        applyList(getResources().getStringArray(R.array.night1));
        return inflater.inflate(R.layout.fragment_night_root, container, false);
    }

    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }
}
