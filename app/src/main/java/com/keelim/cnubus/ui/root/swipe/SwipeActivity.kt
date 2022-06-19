package com.keelim.cnubus.ui.root.swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.databinding.ActivitySwipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySwipeBinding
    private val mode by lazy { intent.getStringExtra("mode") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySwipeBinding>(
            this,
            R.layout.activity_swipe
        ).apply {

        }.also {
            binding = it
        }
        initFlow()
        initData()
    }

    private fun initFlow(){

    }
    private fun initData(){

    }

    private fun handleMigrateSuccess(data: List<Location>) {
        if(data.isEmpty()) {

        } else {

        }
    }
}
