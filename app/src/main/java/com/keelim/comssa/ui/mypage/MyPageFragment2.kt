package com.keelim.comssa.ui.mypage

import android.content.Intent
import android.net.Uri
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
          MyPageScreen(
            sectionClick = { packageName ->
              runCatching {
                  requireActivity()
                    .packageManager
                    .getLaunchIntentForPackage(packageName)
                    ?.let(::startActivity)
                }
                .onFailure {
                  startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                  )
                }
            }
          )
        }
      }
    }
  }
}
