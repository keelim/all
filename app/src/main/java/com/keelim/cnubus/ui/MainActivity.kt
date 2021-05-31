/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.keelim.cnubus.R
import com.keelim.cnubus.base.BaseActivity
import com.keelim.cnubus.databinding.ActivityMainBinding
import com.keelim.cnubus.utils.BackPressCloseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val mainViewModel by viewModels<MainViewModel>()
    lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCloseHandler = BackPressCloseHandler(this)
//        initBottomAppBar()
        createNotification()

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
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }

//    private fun initBottomAppBar() {
//        binding.bottomAppBar.setNavigationOnClickListener {
//            Snackbar.make(binding.root, "기능 준비 중입니다", Snackbar.LENGTH_SHORT).show()
//        }
//
//        binding.bottomAppBar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.more -> {
//                    showMoreOptions()
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }
//    }


    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = getString(R.string.my_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("충남대 버스")
            .setContentText("앱 실행 중")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)

        getSystemService(NotificationManager::class.java).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel(channel)
            }
            notify(0, notificationBuilder.build())
        }
    }
//    private fun navController() = findNavController(R.id.nav_host_fragment)

//    private fun showMoreOptions() = navController().navigate(R.id.more)
}
