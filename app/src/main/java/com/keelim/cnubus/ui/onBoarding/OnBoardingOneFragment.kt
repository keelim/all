package com.keelim.cnubus.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.keelim.cnubus.databinding.FragmentOnBoardingBinding
import com.keelim.cnubus.ui.main.MainActivity


class OnBoardingOneFragment(
    private val section:Int,
    private val imageUrl: String,
) : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding){
        btnSkips.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }

        imgOnBoarding.load(imageUrl)
    }
}