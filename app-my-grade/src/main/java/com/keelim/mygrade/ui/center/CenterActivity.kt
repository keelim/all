package com.keelim.mygrade.ui.center

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.keelim.common.extensions.toast
import com.keelim.data.db.entity.History
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.ActivityCenterBinding
import com.keelim.mygrade.notification.NotificationBuilder
import com.keelim.mygrade.utils.Keys
import com.keelim.mygrade.utils.Keys.IN_APP_UPDATE_REQUEST_CODE
import com.keelim.mygrade.work.MainWorker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CenterViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    fun saveHistory(query: String) = viewModelScope.launch {
        ioRepository.insertHistories(History("", 0, 0f, 0f, 0, 0f, "1"))
    }
}

@AndroidEntryPoint
class CenterActivity : AppCompatActivity() {
    @Inject
    lateinit var notificationBuilder: NotificationBuilder
//    @Inject
//    lateinit var lazyStats: dagger.Lazy<JankStats>

    private val viewModel: CenterViewModel by viewModels()
    private lateinit var binding: ActivityCenterBinding
    private var _forceUpdate: Boolean = false

    private val appPermissions: List<String> = buildList {
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
        DataBindingUtil.setContentView<ActivityCenterBinding>(
            this,
            R.layout.activity_center
        ).apply {
        }.also {
            binding = it
        }
        MainWorker.enqueueWork(this)
        sendNotification()
        handleIntent()
        permissionLauncher.launch(appPermissions.toTypedArray())
        with(Firebase.remoteConfig) {
            getBoolean("forceUpdate").also { forceUpdate ->
                _forceUpdate = forceUpdate
                fetchAndActivate().addOnSuccessListener {
                    if (forceUpdate) {
                        checkInAppUpdate()
                    }
                }
            }
            get("newUpdate").also { newUpdate ->
                toast(
                    when {
                        (newUpdate.asLong().toInt()) > 17 -> "새로운 업데이트가 있습니다. 확인해주세요"
                        (newUpdate.asLong().toInt()) == 17 -> "올바른 버전 입니다."
                        else -> "버전 확인이 완료되었습니다."
                    }
                )
            }
        }
    }

    private fun sendNotification() {
        val replyLabel = "Enter your reply here"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).setLabel(replyLabel).build()

        val resultIntent = Intent(this, CenterActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val icon =
            IconCompat.createWithResource(this@CenterActivity, android.R.drawable.ic_dialog_info)

        val replyAction = NotificationCompat.Action.Builder(icon, "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput).build()

//        notificationBuilder.showNotification(replyAction)
    }

    private fun handleIntent() {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        remoteInput?.let {
            val inputString = it.getCharSequence(KEY_TEXT_REPLY).toString()
            viewModel.saveHistory(inputString)
        }
    }

    private fun checkInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.updatePriority() >= 4 &&
                /* high priority */
                appUpdateInfo.isUpdateTypeAllowed(
                        AppUpdateType.IMMEDIATE
                    )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    Keys.IN_APP_UPDATE_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IN_APP_UPDATE_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_CANCELED -> {
                        if (_forceUpdate) {
                            finishAffinity()
                        }
                    }
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        lazyStats.get().isTrackingEnabled = true
//    }
//
//    override fun onPause() {
//        super.onPause()
//        lazyStats.get().isTrackingEnabled = false
//    }

    companion object {
        const val notificationId = 101
        const val KEY_TEXT_REPLY = "key_text_reply"
    }
}
