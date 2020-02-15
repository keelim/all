package com.keelim.cnubus.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.keelim.cnubus.R
import com.keelim.cnubus.model.ViewPagerAdapter
import com.keelim.cnubus.view.recycler.RecyclerActivity
import com.keelim.cnubus.view.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer.*

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID))
        val adRequest =
            AdRequest.Builder().build()

        adView.loadAd(adRequest)
        pagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)

        main_drawer_button.setOnClickListener {
            if(!drawer_container.isDrawerOpen(GravityCompat.END)) drawer_container.openDrawer(GravityCompat.END)
        }
        drawer_close.setOnClickListener {
            if (drawer_container.isDrawerOpen(GravityCompat.END)) {
                drawer_container.closeDrawer(GravityCompat.END)
            }
        }

        drawer_root_check.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.busurl))))
        }

        drawer_setting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        drawer_lab1.setOnClickListener {
            startActivity(Intent(this,RecyclerActivity::class.java))
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
            }
        }
    }
}