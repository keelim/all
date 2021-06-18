package com.keelim.nandadiagnosis.ui.list

import androidx.fragment.app.viewModels
import com.keelim.common.toast
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ListFragment:BaseFragment<ListFragmentViewModel, FragmentListBinding>() {
    override val viewModel: ListFragmentViewModel by viewModels()

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)


    override fun observeData() = viewModel.nandaListState.observe(this){
        when(it){
            is NandaListState.Error -> {
                initViews(binding)
            }
            is NandaListState.Loading -> {
                handleLoadingState()
            }
            is NandaListState.Success -> {
                handleSuccessState(it)
            }
            is NandaListState.UnInitialized -> {
                handleErrorState()
            }
        }
    }



    private fun handleLoadingState() {
        TODO("Not yet implemented")
    }

    private fun handleSuccessState(state: NandaListState.Success) {
        toast("Produce entity")
    }

    private fun handleErrorState() {
        toast("에러가 발생했습니다.")
    }



    private fun initViews(binding:FragmentListBinding) = with(binding){
        viewModel.fetchData()
    }
}