package com.keelim.cnubus.ui.Broot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;

public class BFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BViewModel homeViewModel = ViewModelProviders.of(this).get(BViewModel.class);
        com.keelim.cnubus.databinding.FragmentBrootBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_broot, container, false);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.bList,
                android.R.layout.simple_list_item_1);
        binding.bListview.setAdapter(arrayAdapterA);

        return binding.getRoot();
    }
}