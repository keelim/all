package com.keelim.cnubus.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.ui.SettingActivity
import com.keelim.cnubus.ui.content.ContentActivity
import com.keelim.cnubus.utils.BackPressCloseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backPressCloseHandler = BackPressCloseHandler(this)

        binding.viewpager.adapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        binding.tabLayout.setupWithViewPager(viewpager)

        binding.mainDrawerButton.setOnClickListener {   //todo 네비게이션 드로어로 업데이트 하는 방안 준비
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
            if (!drawer_container.isDrawerOpen(GravityCompat.END)) drawer_container.openDrawer(
                GravityCompat.END
            )
        }

        drawer_close.setOnClickListener {
            if (drawer_container.isDrawerOpen(GravityCompat.END)) {
                drawer_container.closeDrawer(GravityCompat.END)
            }
        }

        drawer_root_check.setOnClickListener { // 공지사항 홈페이지로 넘어간다.
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.notification_uri)))
            )
        }

        drawer_setting.setOnClickListener {// 설정으로 넘어간다. 아직 기능 구현은 하지 않음
            startActivity(Intent(this, SettingActivity::class.java))
        }

        drawer_content.setOnClickListener {// 공지사항 2 // 페이저 슬라이드 이미지 2개
            startActivity(Intent(this, ContentActivity::class.java))
        }

        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo //업데이트 하는 방식 나중에 이해하기 바람
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    2
                )
                Toast.makeText(this@MainActivity, "업데이트를 시작합니다.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this@MainActivity, "최신 버전 어플리케이션 사용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED)
                popupSnackBarForCompleteUpdate()
        }
        appUpdateManager.registerListener(listener)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                Snackbar.make(binding.drawerContainer, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG)
                    .show()
            }
            RESULT_CANCELED -> {
                Snackbar.make(binding.drawerContainer, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG)
                    .show()
            }
            RESULT_IN_APP_UPDATE_FAILED -> {
                Snackbar.make(binding.drawerContainer, "시스템 오류가 발생했습니다.", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(binding.drawerContainer, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction("RESTART") { appUpdateManager.completeUpdate() }
                setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
                show()
            }
    }

    override fun onBackPressed() {
        if (binding.drawerContainer.isDrawerOpen(GravityCompat.START)) {
            binding.drawerContainer.closeDrawer(GravityCompat.START)
        } else
            backPressCloseHandler.onBackPressed()
    }
}