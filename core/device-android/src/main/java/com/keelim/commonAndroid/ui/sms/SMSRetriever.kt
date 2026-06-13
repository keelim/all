package com.keelim.commonAndroid.ui.sms

import android.content.Context
import android.content.IntentFilter
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.keelim.commonAndroid.receiver.SmsBroadcastReceiver


@Composable
fun SMSRetriever(
    onUpdateOtp: (String?) -> Unit,
) {
    val activity = LocalActivity.current
    if (activity != null) {
        DisposableEffect(activity) {
            SmsRetriever.getClient(activity).startSmsRetriever()

            val receiver = SmsBroadcastReceiver().apply {
                setOtpListener { value ->
                    onUpdateOtp.invoke(value)
                }
            }
            ContextCompat.registerReceiver(
                activity,
                receiver,
                IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
                Context.RECEIVER_EXPORTED
            )
            onDispose {
                activity.unregisterReceiver(receiver)
            }
        }
    }
}
