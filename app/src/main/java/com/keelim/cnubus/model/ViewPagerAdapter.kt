package com.keelim.cnubus.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.keelim.cnubus.view.main.ARootFragment
import com.keelim.cnubus.view.main.BRootFragment
import com.keelim.cnubus.view.main.CRootFragment
import com.keelim.cnubus.view.main.NightRootFragment
import java.util.*

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    private var fragments = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "A노선"
            1 -> "B노선"
            2 -> "C노선"
            3 -> "야간노선"
            else -> ""
        }
    }

    init {
        fragments.add(ARootFragment())
        fragments.add(BRootFragment())
        fragments.add(CRootFragment())
        fragments.add(NightRootFragment())
    }
}