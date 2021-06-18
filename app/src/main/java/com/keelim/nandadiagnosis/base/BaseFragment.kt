package com.keelim.nandadiagnosis.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

internal abstract class BaseFragment<VM:BaseViewModel, VB: ViewBinding>: Fragment() {
    abstract val viewModel:VM
    protected lateinit var binding:VB

    abstract fun getViewBinding():VB

    private lateinit var fetchJob: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchJob = viewModel.fetchData()
        observeData()
    }

    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive){
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}