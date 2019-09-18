package com.keelim.nandadiagnosis.ui.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DomainPageAdapter extends FragmentStatePagerAdapter {


    public DomainPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DomainSectionOne();
            case 1:
                return new DomainSectionTwo();
            case 2:
                return new DomainSectionThree();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "one";
            case 1:
                return "two";
            case 2:
                return "three";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}