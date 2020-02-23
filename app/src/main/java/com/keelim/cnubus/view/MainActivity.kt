package com.keelim.cnubus.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.cnubus.R
import com.keelim.cnubus.model.ViewPagerAdapter
import com.keelim.cnubus.view.recycler.RecyclerActivity
import com.keelim.cnubus.view.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.layout_drawer.*

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID))
        val adRequest =
            AdRequest.Builder().build()

        adView.loadAd(adRequest)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)


        main_drawer_button.setOnClickListener {
            if (!drawer_container.isDrawerOpen(GravityCompat.END)) drawer_container.openDrawer(
                GravityCompat.END
            )
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
            startActivity(Intent(this, RecyclerActivity::class.java))
        }

        drawer_lab2.setOnClickListener {
            startActivity(Intent(this, MapsLabActivity::class.java))
        }

        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    2
                )
                Snackbar.make(drawer_container, "업데이트를 시작합니다.", Snackbar.LENGTH_SHORT).show()
                main_progress.visibility = View.VISIBLE
            } else Snackbar.make(
                drawer_container, "최신 버전 어플리케이션 사용해주셔서 감사합니다.", Snackbar.LENGTH_SHORT).show()
        }

        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED)
                popupSnackbarForCompleteUpdate()
        }
        appUpdateManager.registerListener(listener)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            when (resultCode) {
                RESULT_OK -> {
                    Snackbar.make(drawer_container, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG)
                }
                Activity.RESULT_CANCELED -> {
                    Snackbar.make(drawer_container, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Snackbar.make(drawer_container, "시스템 오류가 발생했습니다.", Snackbar.LENGTH_LONG)
                }
            }
            main_progress.visibility = View.INVISIBLE
        }
    }

    private fun popupSnackbarForCompleteUpdate() {

        Snackbar.make(drawer_container, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
            show()
        }
    }

}