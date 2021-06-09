/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keelim.cnubus.ui.root.aroot.ARootFragment
import com.keelim.cnubus.ui.root.broot.BRootFragment
import com.keelim.cnubus.ui.root.croot.CRootFragment
import com.keelim.cnubus.ui.root.night.NightRootFragment
import com.keelim.cnubus.ui.setting.SettingsFragment
import java.util.ArrayList

class ViewPager2Adapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private var fragments = ArrayList<Fragment>()
    override fun getItemCount(): Int { return 5 }

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
