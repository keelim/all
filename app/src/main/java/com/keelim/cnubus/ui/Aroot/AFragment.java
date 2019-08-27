package com.keelim.cnubus.ui.Aroot;

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

public class AFragment extends Fragment {

    private ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AViewModel aViewModel = ViewModelProviders.of(this).get(AViewModel.class);

        View root = inflater.inflate(R.layout.fragment_aroot, container, false);
        listView = root.findViewById(R.id.a_listview);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.aList,
                android.R.layout.simple_list_item_1);

        listView.setAdapter(arrayAdapterA);


        return root;
    }
}