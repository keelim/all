package com.keelim.cnubus.mainfragment.croot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;

public class CFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CViewModel homeViewModel = ViewModelProviders.of(this).get(CViewModel.class);
        View root = inflater.inflate(R.layout.fragment_croot, container);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.cList,
                android.R.layout.simple_list_item_1);

        ListView listView = container.findViewById(R.id.c_listview);
        listView.setAdapter(arrayAdapterA);
        return root;
    }
}