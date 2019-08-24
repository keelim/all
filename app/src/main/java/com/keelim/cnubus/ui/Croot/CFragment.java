package com.keelim.cnubus.ui.Croot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;
import com.keelim.cnubus.ui.Broot.BViewModel;

public class CFragment extends Fragment {

    private CViewModel cViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BViewModel homeViewModel = ViewModelProviders.of(this).get(BViewModel.class);
        View root = inflater.inflate(R.layout.fragment_croot, container, false);
        return root;
    }
}