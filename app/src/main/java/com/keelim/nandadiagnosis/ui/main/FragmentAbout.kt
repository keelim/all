package com.keelim.nandadiagnosis.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentAboutBinding
import com.keelim.nandadiagnosis.ui.open.OpenSourceActivity

class FragmentAbout: Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.packageName?.let {
            val packageInfo = context?.packageManager?.getPackageInfo(it, 0)
            val versionName = packageInfo?.versionName
            binding.versionNumberTextView.text = versionName
            if (versionName?.contains(getString(R.string.beta)) == true) {
                binding.releaseChannelTextView.visibility = View.VISIBLE
                binding.releaseChannelTextView.text = getString(R.string.beta)
            }
            if (versionName?.contains(getString(R.string.alpha)) == true) {
                binding.releaseChannelTextView.visibility = View.VISIBLE
                binding.releaseChannelTextView.text = getString(R.string.alpha)
            }
        }
        binding.github.setOnClickListener {
            startActivity(Intent("https:www.github.com/keelim/nandaDiagnosis"))
        }

        binding.openSourceLicensesCard.setOnClickListener {
            startActivity(Intent(requireContext(), OpenSourceActivity::class.java))
        }
    }
}