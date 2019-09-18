package com.keelim.nandadiagnosis.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.ui.search_viewpager.SearchViewPagerAdapter;
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding;
import com.keelim.nandadiagnosis.ui.search_viewpager.ViewPagerFirstFragment;
import com.keelim.nandadiagnosis.ui.search_viewpager.ViewPagerSecondFragment;
import com.keelim.nandadiagnosis.ui.search_viewpager.ViewPagerThirdFragment;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class SearchFragment extends Fragment {
    private SearchViewPagerAdapter mainPageAdapter;
    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        mainPageAdapter = new SearchViewPagerAdapter(getActivity().getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
