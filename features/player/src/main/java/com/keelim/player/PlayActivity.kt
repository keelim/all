package com.keelim.player

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.player.databinding.ActivityPlayBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayActivity: AppCompatActivity() {
    private val binding by lazy { ActivityPlayBinding.inflate(layoutInflater)}
    private val viewModel: PlayViewModel by viewModels()
    private val videoAdapter by lazy {
        VideoAdapter(click = { url, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                (it as PlayerFragment).play(url, title)
            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        initViews()
        observeState()
        getVideoList()
    }

    private fun initViews() = with(binding){
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PlayActivity)
            adapter = videoAdapter
        }
    }

    private fun getVideoList() {
        viewModel.getVideo()
    }

    private fun observeState() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.state.collect {
                when(it){
                    is PlayState.Error -> Unit
                    is PlayState.Loading -> Unit
                    is PlayState.Success -> videoAdapter.submitList(it.data)
                    is PlayState.UnInitialized -> Unit
                }
            }
        }
    }
}