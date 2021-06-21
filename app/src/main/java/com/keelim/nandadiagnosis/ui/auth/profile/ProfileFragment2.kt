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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class ProfileFragment2 @Inject constructor(
  private val auth: FirebaseAuth,
) : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {
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

  private val loginLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
          task.getResult(ApiException::class.java)?.let { account ->
            Timber.d("firebase with google ${account.id}")
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
      is ProfileState.Login -> TODO()
      is ProfileState.Success -> handleSuccess(it)
      is ProfileState.Error -> handleError()
    }
  }


  private fun initViews() = with(binding) {
    loginButton.setOnClickListener {
      signInGoogle()
    }

    logoutButton.setOnClickListener {

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
      ProfileState.Success.NotRegistered -> {

      }
      is ProfileState.Success.Registered -> {
        profileGroup.isGone = true
        loginRequiredGroup.isVisible = true
      }
    }
  }

  private fun handleError() {

  }
}
