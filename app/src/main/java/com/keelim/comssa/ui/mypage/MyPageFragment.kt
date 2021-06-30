package com.keelim.comssa.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.data.model.ReviewedData
import com.keelim.comssa.databinding.FragmentMyPageBinding
import com.keelim.comssa.extensions.dip
import com.keelim.comssa.extensions.toGone
import com.keelim.comssa.extensions.toVisible
import com.keelim.comssa.ui.home.GridSpacingItemDecoration

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val myPageAdapter = MyPageAdapter(onMovieClickListener = {
        val action = MyPageFragmentDirections.actionMyPageFragmentToReviewsFragment(it)
        findNavController().navigate(action)
    }
    )

    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeReviewedData()
        viewModel.fetchReviewedData()
    }

    private fun showLoadingIndicator() {
        binding.progressBar.toVisible()
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.toGone()
    }

    private fun showNoDataDescription(message: String) {
        binding.recyclerView.toGone()
        binding.descriptionTextView.toVisible()
        binding.descriptionTextView.text = message
    }

    private fun showErrorDescription(message: String) {
        binding.recyclerView.toGone()
        binding.descriptionTextView.toVisible()
        binding.descriptionTextView.text = message
    }

    private fun showReviewedMovies(reviewedMovies: List<ReviewedData>) {
        myPageAdapter.apply {
            this.reviewedMovies = reviewedMovies
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding.recyclerView.apply {
            adapter = myPageAdapter
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(3, dip(6f)))
        }

        (binding.recyclerView.adapter as? MyPageAdapter)?.apply {
            onMovieClickListener = { data ->
                val action = MyPageFragmentDirections.actionMyPageFragmentToReviewsFragment(data)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeReviewedData() = viewModel.reviewedData.observe(viewLifecycleOwner, {
        try {
            showLoadingIndicator()
            if (it.isNullOrEmpty()) {
                showNoDataDescription("아직 리뷰한 영화가 없어요.\n홈 탭을 눌러 영화를 리뷰해보세요 🙌")
            } else {
                showReviewedMovies(it)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorDescription("에러가 발생했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    })
}