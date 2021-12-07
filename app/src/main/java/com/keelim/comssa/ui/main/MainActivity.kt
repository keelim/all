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
package com.keelim.comssa.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.webkit.URLUtil
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.keelim.comssa.R
import com.keelim.comssa.databinding.ActivityMainBinding
import com.keelim.comssa.databinding.ItemPasswordBinding
import com.keelim.comssa.di.download.DownloadReceiver
import com.keelim.comssa.di.download.DownloadRequest
import com.keelim.comssa.extensions.toast
import com.keelim.comssa.ui.feed.FeedFragment
import com.keelim.comssa.ui.main.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }
    @Inject
    lateinit var recevier: DownloadReceiver
    @Inject
    lateinit var downloadRequest: DownloadRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        fileChecking()
        observeDownloadLink()
    }

    private fun initViews() = with(binding) {
        val fragmentList = listOf(
            SearchFragment(),
            FeedFragment(),
            SearchFragment(),
            SearchFragment(),
            SearchFragment()
        )
        viewPagerAdapter.fragmentList.addAll(fragmentList)
        viewpagerMain.adapter = viewPagerAdapter

        viewpagerMain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigationMain.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavigationMain.setOnItemSelectedListener {
            binding.viewpagerMain.currentItem = when (it.itemId) {
                R.id.menu_feed -> FEED_FRAGMENT
                R.id.menu_recommend -> RECOMMEND_FRAGMENT
                R.id.menu_write -> WRITE_FRAGMENT
                R.id.menu_alarm -> ALARM_FRAGMENT
                else -> PROFILE_FRAGMENT
            }
            return@setOnItemSelectedListener true
        }

        imageviewMainWrite.setOnClickListener {
            viewpagerMain.currentItem = WRITE_FRAGMENT
        }

        bottomNavigationMain.itemIconTintList = null
    }

    private fun fileChecking() {
        val check = File(getExternalFilesDir(null), "comssa.db")
        if (check.exists().not())
            databaseDownloadAlertDialog()
        else
            toast("데이터베이스가 존재합니다. 그대로 진행 합니다")
    }

    private fun databaseDownloadAlertDialog() {
        val itemPassword = ItemPasswordBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("다운로드 요청")
            .setView(itemPassword.root)
            .setMessage("어플리케이션 사용을 위해 데이터베이스를 다운로드 합니다.")
            .setPositiveButton("ok") { dialog, which ->
                if(itemPassword.password.text.toString() == getString(R.string.password)){
                    toast("서버로부터 데이터 베이스를 요청 합니다.")
                    downloadDatabase()
                } else{
                    toast("디폴트 데이터베이스를 다운로드 받습니다.")
                    downloadDatabase()
                }
            }
            .show()
    }

    private fun downloadDatabase(link: String? = null) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(recevier, IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        })
        downloadManager.enqueue(
            downloadRequest.provideDownloadRequest(link)
        )
    }

    private fun observeDownloadLink() = lifecycleScope.launchWhenStarted {
        viewModel.downloadLink.collect {
            if (it.isNotBlank() && URLUtil.isValidUrl(it)) {
                downloadDatabase(it)
            }
        }
    }

    private companion object {
        const val FEED_FRAGMENT = 0
        const val RECOMMEND_FRAGMENT = 1
        const val WRITE_FRAGMENT = 2
        const val ALARM_FRAGMENT = 3
        const val PROFILE_FRAGMENT = 4
    }

    inner class MainViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
        val fragmentList:MutableList<Fragment> = mutableListOf()
        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int) = fragmentList[position]
    }
}
