package com.keelim.comssa.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keelim.comssa.data.model.Feed
import com.keelim.comssa.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment @Inject constructor() : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedViewModel by viewModels()
    private val feedAdapter by lazy {
        FeedAdapter {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFeedState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        binding.recyclerviewFeed.adapter = feedAdapter
        feedAdapter.submitList(
            listOf(
                Feed(0, 0, "title", "contnet", "date", false, true, 6, 6),
                Feed(1, 1, "title1", "contnet1", "date1", false, true, 7, 8),
                Feed(2, 2, "title2", "contnet2", "date2", false, true, 6, 6)
            )
        )
        feedAdapter.notifyDataSetChanged()
    }

    private fun observeFeedState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.state.collect {
            when (it) {
                is FeedState.Success -> {
                    feedAdapter.submitList(it.data)
                }

                is FeedState.Error -> {
                }

                else -> Unit
            }
        }
    }
}
