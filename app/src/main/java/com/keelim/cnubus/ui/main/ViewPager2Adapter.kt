package com.keelim.cnubus.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.ui.main.aroot.ARootFragment
import com.keelim.cnubus.ui.main.broot.BRootFragment
import com.keelim.cnubus.ui.main.croot.CRootFragment
import com.keelim.cnubus.ui.main.night.NightRootFragment
import com.keelim.cnubus.ui.main.setting.SettingsFragment
import java.util.*

class ViewPager2Adapter(fa:FragmentActivity) :FragmentStateAdapter(fa){
    private var fragments = ArrayList<Fragment>()
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    init {
        fragments.add(ARootFragment())
        fragments.add(BRootFragment())
        fragments.add(CRootFragment())
        fragments.add(NightRootFragment())
        fragments.add(SettingsFragment())
    }

}