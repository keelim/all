package com.keelim.nandadiagnosis.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.databinding.FragmentProfileBinding
import com.keelim.nandadiagnosis.ui.main.favorite.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor(
    private val auth: FirebaseAuth,
    ) : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val scope = MainScope()
    private lateinit var db: AppDatabaseV2
    private lateinit var adapter: FavoriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth.currentUser ?: run { findNavController().navigateUp() }

        adapter = FavoriteAdapter()

        db = AppDatabaseV2.getInstance(requireContext())!!
        initView()
        initEvent()
    }

    private fun initView() {
        with(binding) {
            userId.text = auth.currentUser?.displayName
        }
    }

    private fun initEvent() {
        with(binding) {
            favoriteContainer.setOnClickListener {
                findNavController().navigate(R.id.favoriteFragment2)
            }
        }
    }

    private fun initData() {
        scope.launch {
            val items = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                db.dataDao.favorites()
            }

            if (items.isEmpty()){
                toast("아직 관심으로 지정한 영역이 없습니다.")
            }

            CoroutineScope(Dispatchers.Main).launch {
                if (items.isEmpty()){
                    binding.profileRecycler.visibility = View.GONE
                    binding.noText.visibility = View.VISIBLE
                } else{
                    adapter.submitList(items)
                }
            }
        }
    }
}