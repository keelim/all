package com.keelim.arducon.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.arducon.ui.screen.main.MainRoute
import com.keelim.arducon.ui.screen.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelCallback()
        setContent {
            MainRoute(
                viewModel = viewModel,
            )
        }
    }

    private fun initViewModelCallback() {
        viewModel.onClickSearch
            .flowWithLifecycle(lifecycle)
            .filterNot { it.isEmpty() }
            .onEach { uri ->
                try {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri)
                    ).let { startActivity(it) }
                    viewModel.clear()
                } catch (e: Exception) {
                    Toast.makeText(this, "Exception !!!\n" + e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }.launchIn(lifecycleScope)
    }
}
