package com.keelim.nandadiagnosis.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding;
import com.keelim.nandadiagnosis.ui.category.category_viewpager.CategoryViewPagerAdapter;
import com.keelim.nandadiagnosis.ui.category.category_viewpager.ViewPagerFirstFragment;
import com.keelim.nandadiagnosis.ui.category.category_viewpager.ViewPagerSecondFragment;
import com.keelim.nandadiagnosis.ui.category.category_viewpager.ViewPagerThirdFragment;

import java.util.Objects;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class CategoryFragment extends Fragment {
    private CategoryViewPagerAdapter mainPageAdapter;
    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        mainPageAdapter = new CategoryViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerSetting();
        return binding.getRoot();
    }


    private void viewPagerSetting() {
        mainPageAdapter.addItem(new ViewPagerFirstFragment());
        mainPageAdapter.addItem(new ViewPagerSecondFragment());
        mainPageAdapter.addItem(new ViewPagerThirdFragment());
        binding.searchViewpager.setAdapter(mainPageAdapter);
    }

}
