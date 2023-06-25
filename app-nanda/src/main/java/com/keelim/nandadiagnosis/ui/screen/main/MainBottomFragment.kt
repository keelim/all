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
package com.keelim.nandadiagnosis.ui.screen.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.common.extensions.toast
import com.keelim.composeutil.setThemeContent
import com.keelim.nandadiagnosis.R

class MainBottomFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = setThemeContent {
        MainBottomSheetRoute(
            onBlogClick = {
                dismiss()
                findNavController().navigate(R.id.inAppWebFragment)
            },
            onFavoriteClick = {
                dismiss()
                findNavController().navigate(R.id.favoriteFragment2)
            },
            onOpenSourceClick = {
                try {
                    dismiss()
                    startActivity(Intent(requireContext(), OssLicensesActivity::class.java))
                } catch (throwable: Throwable) {
                    throwable.localizedMessage?.let { toast(it) }
                }
            },
            onAboutClick = {
                dismiss()
                findNavController().navigate(R.id.aboutFragment)
            },
        )
    }
}
