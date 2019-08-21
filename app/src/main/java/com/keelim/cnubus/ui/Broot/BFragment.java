package com.keelim.cnubus.ui.Broot;

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

public class BFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BViewModel homeViewModel = ViewModelProviders.of(this).get(BViewModel.class);
        View root = inflater.inflate(R.layout.fragment_aroot, container, false);

        return root;
    }
}