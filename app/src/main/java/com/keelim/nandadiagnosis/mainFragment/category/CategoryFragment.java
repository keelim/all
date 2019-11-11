package com.keelim.nandadiagnosis.mainFragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding;
import com.keelim.nandadiagnosis.mainFragment.category.category_viewpager.CategoryView1;
import com.keelim.nandadiagnosis.mainFragment.category.category_viewpager.CategoryView2;
import com.keelim.nandadiagnosis.mainFragment.category.category_viewpager.CategoryViewPagerAdapter;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class CategoryFragment extends Fragment {
    private CategoryViewPagerAdapter mainPageAdapter;
    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        mainPageAdapter = new CategoryViewPagerAdapter(getActivity().getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerSetting();
        return binding.getRoot();
    }


    private void viewPagerSetting() {
        mainPageAdapter.addItem(new CategoryView1());
        mainPageAdapter.addItem(new CategoryView2());
        binding.searchViewpager.setAdapter(mainPageAdapter);
    }

}
