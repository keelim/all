package com.keelim.nandadiagnosis.mainfragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
