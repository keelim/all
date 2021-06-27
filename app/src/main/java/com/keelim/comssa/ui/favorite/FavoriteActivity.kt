package com.keelim.comssa.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.comssa.databinding.ActivityFavoriteBinding
import com.keelim.comssa.extensions.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity: AppCompatActivity() {
    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater) }

    private val viewModel:FavoriteViewModel by viewModels()
    private val favoriteAdapter = FavoriteAdapter(
        click = { favorite, id ->
            viewModel.favorite(favorite, id)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        viewModel.fetchData()
    }
    private fun observeData() = viewModel.favoriteState.observe(this){
        when(it){
            is FavoriteState.UnInitialized -> {
                initViews()
            }
            is FavoriteState.Loading -> toast("데이터 로딩 중 입니다.")
            is FavoriteState.Success -> handleSuccess(it)
            is FavoriteState.Error -> toast("에러가 발생하였습니다. 뒤로가기를 눌러주세요")
        }
    }

    private fun initViews() = with(binding){
        favoriteRecycler.adapter = favoriteAdapter
        favoriteRecycler.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        favoriteAdapter.submitList(listOf())
    }

    private fun handleSuccess(state:FavoriteState.Success){
        favoriteAdapter.submitList(state.data)
    }
}