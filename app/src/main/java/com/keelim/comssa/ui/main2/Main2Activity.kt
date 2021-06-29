package com.keelim.comssa.ui.main2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivityMain2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Main2Activity: AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding){
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.reviewsFragment, R.id.myPageFragment)
        )
        setupActionBarWithNavController(navController(), appBarConfiguration)
        navView.setupWithNavController(navController())
    }

    private fun navController() = findNavController(R.id.nav_host_fragment)
}