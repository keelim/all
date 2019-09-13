package com.keelim.cnubus.ui.Croot;

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
import com.keelim.cnubus.databinding.FragmentCrootBinding;

public class CFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CViewModel homeViewModel = ViewModelProviders.of(this).get(CViewModel.class);
        com.keelim.cnubus.databinding.FragmentCrootBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_croot, container, false);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.cList,
                android.R.layout.simple_list_item_1);

        binding.cListview.setAdapter(arrayAdapterA);
        return binding.getRoot();
    }
}