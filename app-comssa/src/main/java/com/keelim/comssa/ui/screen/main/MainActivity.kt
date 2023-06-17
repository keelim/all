/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.comssa.ui.screen.main

import android.Manifest
import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.keelim.commonAndroid.core.AppMainDelegator
import com.keelim.commonAndroid.core.AppMainViewModel
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivityMainBinding
import com.keelim.comssa.databinding.ItemPasswordBinding
import com.keelim.comssa.ui.screen.main.flash.FlashCardFragment
import com.keelim.comssa.ui.screen.main.search.SearchFragment
import com.keelim.comssa.ui.screen.mypage.MyPageFragment2
import com.keelim.comssa.utils.toast
import com.keelim.data.di.download.DownloadReceiver
import com.keelim.data.di.download.DownloadRequest
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: AppMainViewModel by viewModels()
    private val appMainDelegator by lazy { AppMainDelegator(this, viewModel) }
    private val mainViewPagerAdapter by lazy { MainViewPagerAdapter(this) }

    @Inject lateinit var recevier: DownloadReceiver

    @Inject lateinit var downloadRequest: DownloadRequest

    private val appPermissions: List<String> = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.READ_MEDIA_IMAGES)
            add(Manifest.permission.READ_MEDIA_VIDEO)
            add(Manifest.permission.READ_MEDIA_AUDIO)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter { appPermissions.contains(it.key) }
            if (responsePermissions.filter { it.value }.size == appPermissions.size) {
                toast("권한이 확인되었습니다.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fileChecking()
        initViews()
        permissionLauncher.launch(appPermissions.toTypedArray())
    }

    private fun initViews() = with(binding) {
        with(viewpagerMain) {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.bottomNavigationMain.menu.getItem(position).isChecked = true
                }
            })
            adapter = mainViewPagerAdapter
        }

        with(bottomNavigationMain) {
            setOnItemSelectedListener {
                viewpagerMain.currentItem = when (it.itemId) {
                    R.id.menu_feed -> FEED_FRAGMENT
                    R.id.menu_recommend -> RECOMMEND_FRAGMENT
                    R.id.menu_write -> WRITE_FRAGMENT
                    R.id.menu_alarm -> ALARM_FRAGMENT
                    else -> PROFILE_FRAGMENT
                }
                return@setOnItemSelectedListener true
            }
        }
        with(imageviewMainWrite) {
            setOnClickListener { viewpagerMain.currentItem = WRITE_FRAGMENT }
        }
        with(bottomNavigationMain) { itemIconTintList = null }
    }

    private fun fileChecking() {
        File(getExternalFilesDir(null), "comssa.db")
            .takeUnless { file -> file.exists() }
            .also { databaseDownloadAlertDialog() }
    }

    private fun databaseDownloadAlertDialog() {
        val itemPassword = ItemPasswordBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("다운로드 요청")
            .setView(itemPassword.root)
            .setMessage("어플리케이션 사용전 데이터베이스를 다운로드합니다.")
            .setPositiveButton("ok") { dialog, which ->
                toast(
                    if (itemPassword.password.text.toString() == getString(R.string.password)) {
                        "서버로부터 데이터베이스를 요청 합니다."
                    } else {
                        "디폴트 데이터베이스를 다운로드받습니다."
                    },
                )
                downloadDatabase("")
            }
            .show()
    }

    private fun downloadDatabase(link: String) {
        if (URLUtil.isValidUrl(link).not()) {
            toast("유효하지 않는 URL 입니다. 앱을 다시 시작해주세요")
            return
        }
        ContextCompat.registerReceiver(
            this,
            recevier,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        getSystemService(DownloadManager::class.java).enqueue(
            downloadRequest.provideDownloadRequest(link),
        )
    }

    inner class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val fragmentList: List<Fragment> = listOf(
            SearchFragment(),
            SearchFragment(),
            FlashCardFragment(),
            SearchFragment(),
            MyPageFragment2(),
        )

        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int) = fragmentList[position]
    }

    companion object {
        const val FEED_FRAGMENT = 0
        const val RECOMMEND_FRAGMENT = 1
        const val WRITE_FRAGMENT = 2
        const val ALARM_FRAGMENT = 3
        const val PROFILE_FRAGMENT = 4
    }
}
