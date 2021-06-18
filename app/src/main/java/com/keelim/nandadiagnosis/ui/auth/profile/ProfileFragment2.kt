package com.keelim.nandadiagnosis.ui.auth.profile

import androidx.fragment.app.viewModels
import com.keelim.nandadiagnosis.base.BaseFragment
import com.keelim.nandadiagnosis.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ProfileFragment2: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {
    override val viewModel: ProfileViewModel by viewModels()

    override fun getViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)


    override fun observeData() {
        TODO("Not yet implemented")
    }
}