package com.keelim.comssa.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keelim.comssa.databinding.FragmentReviewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment: Fragment() {
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel:ReviewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() = viewModel.text.observe(viewLifecycleOwner, {
        binding.textNotifications.text = it
    })
}