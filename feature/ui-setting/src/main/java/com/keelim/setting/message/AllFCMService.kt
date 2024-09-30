package com.keelim.setting.message

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.keelim.common.di.ApplicationScope
import com.keelim.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AllFCMService : FirebaseMessagingService() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    private var alarmActionJob: Job? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("newToken: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        alarmActionJob = applicationScope.launch {
            alarmRepository.insertAlarm(
                title = remoteMessage.notification?.title ?: "title",
                subTitle = remoteMessage.notification?.body ?: "body",
            )
        }.apply {
            invokeOnCompletion {
                alarmActionJob = null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmActionJob?.cancel()
        alarmActionJob = null
    }
}
