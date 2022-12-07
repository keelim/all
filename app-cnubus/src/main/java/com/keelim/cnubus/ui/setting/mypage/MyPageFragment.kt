/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.setting.mypage

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentMyPageBinding
import com.keelim.common.extensions.toInvisible
import com.keelim.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var binding: FragmentMyPageBinding
    private val viewModel by viewModels<MyPageViewModel>()

    private val historyAdapter by lazy {
        MyPageHistoryAdapter { history ->
            viewModel.deleteHistory(history)
            viewModel.init()
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            FirebaseAuth.getInstance().currentUser
                ?.let { user ->
                    binding.btnGoogleLogin.toInvisible()
                }
        } else {
            Timber.e("로그인의 실패하였습니다.")
            toast("로그인의 실패하였습니다.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)
        initData()
        initViews()
        observeState()
    }

    private fun initData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() = with(binding) {
        ivEditBtn.setOnClickListener {
            findNavController().navigate(R.id.userEditDialog)
        }
        recyclerHistory.run {
            setHasFixedSize(true)
            adapter = historyAdapter
        }
        coverHistoryDetailButton.setOnClickListener {
            Snackbar.make(binding.root, "전부 지우기겠습니까?", Snackbar.LENGTH_SHORT).run {
                setAction("Ok") {
                    viewModel?.let {
                        it.deleteHistoryAll()
                    }
                }
                show()
            }
        }
        btnGoogleLogin.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.viewEvent
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { viewEvent ->
                when (viewEvent) {
                    is MyPageViewModel.ViewEvent.ShowToast -> toast(viewEvent.message)
                }
            }
    }
}
