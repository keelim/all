/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.ui.auth.profile

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentProfileBinding
import com.keelim.nandadiagnosis.ui.main.favorite.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
internal class ProfileFragment2 : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

  override val viewModel: ProfileViewModel by viewModels()

  override fun getViewBinding(): FragmentProfileBinding =
    FragmentProfileBinding.inflate(layoutInflater)

  private val gso by lazy {
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()
  }
  private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }
  private val adapter = FavoriteAdapter()
  private val auth by lazy { Firebase.auth }

  private val loginLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
          task.getResult(ApiException::class.java)?.let { account ->
            Timber.d("firebase with google ${account.id}")
            viewModel.saveToken(account.idToken ?: throw Exception())
          } ?: throw Exception()
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }

  override fun observeData() = viewModel.profileState.observe(this) {
    when (it) {
      is ProfileState.UnInitialized -> initViews()
      is ProfileState.Loading -> handleLoading()
      is ProfileState.Login -> handleLogin(it)
      is ProfileState.Success -> handleSuccess(it)
      is ProfileState.Error -> handleError()
    }
  }

  private fun initViews() = with(binding) {
    profileRecycler.adapter = adapter
    profileRecycler.layoutManager = LinearLayoutManager(requireContext())
    loginButton.setOnClickListener {
      signInGoogle()
    }

    logoutButton.setOnClickListener {
      viewModel.signOut()
    }
  }

  private fun signInGoogle() {
    val signInIntent = gsc.signInIntent
    loginLauncher.launch(signInIntent)
  }

  private fun handleLoading() = with(binding) {
    progressBar.isVisible = true
    loginRequiredGroup.isGone = true
  }

  private fun handleSuccess(state: ProfileState.Success) = with(binding) {
    progressBar.isGone = true
    when (state) {
      is ProfileState.Success.Registered -> {
        handleRegister(state)
      }

      is ProfileState.Success.NotRegistered -> {
        profileGroup.isGone = true
        loginRequiredGroup.isVisible = true
      }
    }
  }

  private fun handleLogin(state: ProfileState.Login) = with(binding) {
    progressBar.isVisible = true
    val credential = GoogleAuthProvider.getCredential(state.token, null)
    auth.signInWithCredential(credential)
      .addOnCompleteListener(requireActivity()) { task ->
        if (task.isSuccessful) {
          val user = auth.currentUser
          viewModel.setUser(user)
        } else {
          viewModel.setUser(null)
        }
      }
  }

  private fun handleRegister(state: ProfileState.Success.Registered) = with(binding) {
    Timber.w("값 $state")
    profileGroup.isVisible = true
    loginRequiredGroup.isGone = true
    imageView.load(state.profileImage.toString())
    userId.text = state.userName

    if (state.favoriteList.isEmpty()) {
      noText.isGone = false
    } else {
      noText.isGone = true
      adapter.submitList(state.favoriteList)
    }
  }

  private fun handleError() {
    requireActivity().toast("에러가 발생했습니다.")
  }
}
