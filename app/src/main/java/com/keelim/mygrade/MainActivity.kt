package com.keelim.mygrade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.mygrade.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        oss.setOnClickListener {
            startActivity((Intent(this@MainActivity, OssLicensesMenuActivity::class.java)))
        }
    }
}