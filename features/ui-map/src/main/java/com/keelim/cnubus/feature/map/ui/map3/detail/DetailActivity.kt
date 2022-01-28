package com.keelim.cnubus.feature.map.ui.map3.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.feature.map.R
import com.keelim.cnubus.feature.map.databinding.ActivityDetailBinding
import com.keelim.common.loadAsync
import com.keelim.common.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleIntent(intent)
        observeState()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) = with(binding) {
        val location: Location? by lazy { intent.getParcelableExtra("item") }
        location?.let {
            title.text = it.name
            imageDetail.loadAsync(it.imgUrl, null)
            tvLatlng.text = it.latLng.toString().slice(IntRange(0, 15))
        }
    }

    private fun observeState() = lifecycleScope.launch {
        repeatCallDefaultOnStarted {

        }
    }
}
