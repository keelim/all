package com.keelim.commonAndroid.message

import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class AllFCMService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("newToken: $token")
    }
}
