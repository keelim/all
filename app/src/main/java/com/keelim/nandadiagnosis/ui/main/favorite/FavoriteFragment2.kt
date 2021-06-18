package com.keelim.nandadiagnosis.ui.main.favorite

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.common.toast
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class FavoriteFragment2 : BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>() {
    private val favoriteAdapter by lazy { FavoriteAdapter() }
    override val viewModel: FavoriteViewModel by viewModels()

    override fun getViewBinding(): FragmentFavoriteBinding =
        FragmentFavoriteBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.favoriteState.observe(this) {
        Timber.d("viewmodel scope $it")
        when (it) {
            is FavoriteListState.UnInitialized -> {
                initViews(binding)
            }
            is FavoriteListState.Success -> {
                handleSuccess(it)
            }
            is FavoriteListState.Loading -> {
                handleLoading()
            }
            is FavoriteListState.Error -> {
                handleError()
            }
        }
    }

    private fun handleError() {
        toast("Error 로딩 화면에 에러가 표시 됩니다.")
    }

    private fun handleSuccess(state: FavoriteListState.Success) {
        toast("$state")
        Timber.d(" 데이터 화면 넘어가기 $state")

        binding.favoriteRecycler.visibility = View.VISIBLE
        favoriteAdapter.apply {
            submitList(state.favoriteList)
        }
    }

    private fun handleLoading() {
        toast("현재 데이터를 불러오는 중입니다.")
    }

    private fun initViews(binding: FragmentFavoriteBinding) = with(binding) {
        favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
        favoriteRecycler.adapter = favoriteAdapter
        viewModel.fetchData()
    }
}