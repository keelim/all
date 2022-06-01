package com.keelim.cnubus.ui.root.swipe

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.databinding.ActivitySwipeBinding
import com.keelim.cnubus.feature.map.ui.MapEvent
import com.keelim.cnubus.feature.map.ui.MapsActivity
import com.keelim.cnubus.ui.root.RootCommonViewModel
import com.keelim.cnubus.ui.root.basic.RootAdapter
import com.keelim.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SwipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySwipeBinding
    private val viewModel by viewModels<RootCommonViewModel>()
    private val mode by lazy { intent.getStringExtra("mode") ?: "" }
    private val rootAdapter by lazy {
        RootAdapter(
            click = { position ->
                viewModel.insertHistory(position, mode)
                val data = when (mode) {
                    "a" -> viewModel.data.value[position].roota
                    "b" -> viewModel.data.value[position].rootb
                    "c" -> viewModel.data.value[position].rootc
                    else -> null
                }
                data?.let { value ->
                    startActivity(
                        Intent(this, MapsActivity::class.java).apply {
                            putExtra("location", value - 1)
                            putExtra("mode", mode)
                        }
                    )
                } ?: toast("노선 준비 중입니다. ")
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySwipeBinding>(
            this,
            R.layout.activity_swipe
        ).apply {
            viewpager2.adapter = rootAdapter
        }.also {
            binding = it
        }
        initFlow()
        initData()
    }

    private fun initFlow(){
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .distinctUntilChanged()
            .onEach { viewEvent ->
                when(viewEvent) {
                    MapEvent.UnInitialized -> Unit
                    MapEvent.Loading -> toast("로딩 중입니다")
                    is MapEvent.MigrateSuccess -> handleMigrateSuccess(viewEvent.data)
                    is MapEvent.Error -> toast(viewEvent.message)
                }
            }
            .catch {

            }
            .launchIn(lifecycleScope)
    }
    private fun initData(){

    }

    private fun handleMigrateSuccess(data: List<Location>) {
        if(data.isEmpty()) {

        } else {

        }
    }
}
