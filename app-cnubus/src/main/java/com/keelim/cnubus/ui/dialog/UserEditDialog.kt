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
package com.keelim.cnubus.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.keelim.cnubus.databinding.DialogUserEditBinding
import com.keelim.cnubus.ui.setting.mypage.MyPageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit
import timber.log.Timber

class UserEditDialog : BottomSheetDialogFragment() {
    private var _binding: DialogUserEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUserEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        editId.textChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val value = it.toString()
                if (value.length > 10) {
                    binding.editId.error = "아이디 길이는 10 이하입니다."
                    binding.btnSubmit.isEnabled = false
                } else if (value.isEmpty()) {
                    binding.editId.error = "아이디가 있어야 합니다."
                    binding.btnSubmit.isEnabled = false
                } else {
                    binding.editId.error = null
                    binding.btnSubmit.isEnabled = true
                }
            }

        btnSubmit.clicks()
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    viewModel.changeUserId(binding.editId.text.toString())
                    findNavController().navigateUp()
                },
                onComplete = {
                    binding.btnSubmit.isEnabled = false
                    binding.editId.setText("")
                },
                onError = {
                    Timber.e("RX ERROR ${it.message}")
                }
            )
    }
}
