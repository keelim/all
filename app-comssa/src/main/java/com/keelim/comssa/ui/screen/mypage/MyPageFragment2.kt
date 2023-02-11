package com.keelim.comssa.ui.screen.mypage

import android.os.Bundle
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import com.keelim.comssa.R
import com.keelim.comssa.databinding.FragmentComposeViewBinding

class MyPageFragment2 : Fragment(R.layout.fragment_compose_view) {
    private lateinit var binding: FragmentComposeViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComposeViewBinding.bind(view)
        binding.composeView.setContent {
            MaterialTheme {
                Surface {
                }
            }
        }
    }
}
