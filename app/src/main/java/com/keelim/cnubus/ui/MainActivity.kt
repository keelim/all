package com.keelim.cnubus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.utils.BackPressCloseHandler

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPressCloseHandler = BackPressCloseHandler(this)

        binding.viewpager.adapter = ViewPager2Adapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "A노선"
                1 -> tab.text = "B노선"
                2 -> tab.text = "C노선"
                3 -> tab.text = "야간노선"
                4 -> tab.text = "설정"
            }
        }.attach()



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
                Toast.makeText(this@MainActivity, "최신 버전 어플리케이션 사용해주셔서 감사합니다.", Toast.LENGTH_SHORT)
                    .show()
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
            RESULT_OK -> Snackbar.make(binding.root, "업데이트를 성공적으로 완료했습니다.", Snackbar.LENGTH_LONG)
                .show()
            RESULT_CANCELED -> Snackbar.make(binding.root, "업데이트를 취소하였습니다.", Snackbar.LENGTH_LONG)
                .show()
            RESULT_IN_APP_UPDATE_FAILED -> Snackbar.make(
                binding.root,
                "시스템 오류가 발생했습니다.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(binding.root, "업데이트를 다운로드 하고 있습니다.", Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction("RESTART") { appUpdateManager.completeUpdate() }
                setActionTextColor(resources.getColor(R.color.colorAccent, this@MainActivity.theme))
                show()
            }
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }
}