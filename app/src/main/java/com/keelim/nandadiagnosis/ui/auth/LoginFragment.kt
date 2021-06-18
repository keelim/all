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
package com.keelim.nandadiagnosis.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!
  private lateinit var auth: FirebaseAuth
  private val callbackManager by lazy { CallbackManager.Factory.create() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    auth = Firebase.auth

    initLogin()
    initSignUp()
    initEmailAndPassword()
    initFacebook()
  }

  private fun initEmailAndPassword() {
    binding.email.addTextChangedListener {
      val enable = binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()
      binding.login.isEnabled = enable
      binding.signup.isEnabled = enable
    }

    binding.password.addTextChangedListener {
      val enable = binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()
      binding.login.isEnabled = enable
      binding.signup.isEnabled = enable
    }
  }

  private fun initSignUp() {
    binding.signup.setOnClickListener {
      val email = email()
      val password = password()

      auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            Toast.makeText(requireContext(), "회원 가입 성공", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(requireContext(), "회원 가입 실패", Toast.LENGTH_SHORT).show()
          }
        }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initLogin() {
    binding.login.setOnClickListener {
      val email = email()
      val password = password()

      auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            handleLogin()
          } else {
            Toast.makeText(requireContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
          }
        }
    }
  }

  private fun initFacebook() {
    binding.facebook.setPermissions("email", "public_profile")
    binding.facebook.registerCallback(
      callbackManager,
      object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
          val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
          auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
              if (task.isSuccessful) {
                handleLogin()
              } else {
                Toast.makeText(requireContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT)
                  .show()
              }
            }
        }

        override fun onCancel() {}

        override fun onError(error: FacebookException?) {
          Toast.makeText(requireActivity(), "페이스북 로그인이 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
      }
    )
    binding.facebook.setOnClickListener {
    }
  }

  private fun handleLogin() {
    auth.currentUser ?: run {
      Toast.makeText(requireContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
      return
    }

    val uid = auth.currentUser?.uid.orEmpty()
    val currentUserDB = Firebase.database.reference.child("Users").child(uid)
    val user = mutableMapOf<String, Any>()
    user["uid"] = uid
    currentUserDB.updateChildren(user)

    findNavController().navigate(R.id.navigation_category)
  }

  private fun email(): String = binding.email.text.toString()
  private fun password(): String = binding.password.text.toString()
}
