package com.keelim.cnubus.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.ui.root.aroot.ARootFragment
import com.keelim.cnubus.ui.root.broot.BRootFragment
import com.keelim.cnubus.ui.root.croot.CRootFragment
import com.keelim.cnubus.ui.root.night.NightRootFragment
import com.keelim.cnubus.ui.setting.SettingsFragment
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