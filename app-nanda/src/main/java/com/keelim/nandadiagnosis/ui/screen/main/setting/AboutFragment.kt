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
package com.keelim.nandadiagnosis.ui.screen.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        activity?.packageName?.let {
            val packageInfo = context?.packageManager?.getPackageInfo(it, 0)
            val versionName = packageInfo?.versionName
            versionNumberTextView.text = versionName
            if (versionName?.contains(getString(R.string.beta)) == true) {
                releaseChannelTextView.visibility = View.VISIBLE
                releaseChannelTextView.text = getString(R.string.beta)
            }
            if (versionName?.contains(getString(R.string.alpha)) == true) {
                releaseChannelTextView.visibility = View.VISIBLE
                releaseChannelTextView.text = getString(R.string.alpha)
            }
        }
        github.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                findNavController().navigate(
                    R.id.webFragment,
                    bundleOf(
                        "web" to getString(R.string.github)
                    )
                )
            }
        }

        openSourceLicensesCard.setOnClickListener {
            startActivity(Intent(requireContext(), OssLicensesActivity::class.java))
        }
    }
}
