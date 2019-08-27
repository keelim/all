package com.keelim.cnubus.ui.Croot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;
import com.keelim.cnubus.ui.Broot.BViewModel;

import java.util.List;

public class CFragment extends Fragment {

    private ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CViewModel homeViewModel = ViewModelProviders.of(this).get(CViewModel.class);
        View root = inflater.inflate(R.layout.fragment_croot, container, false);

        listView = root.findViewById(R.id.c_listview);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.cList,
                android.R.layout.simple_list_item_1);

        listView.setAdapter(arrayAdapterA);
        return root;
    }
}