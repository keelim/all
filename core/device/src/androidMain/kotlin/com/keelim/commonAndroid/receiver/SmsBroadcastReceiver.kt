package com.keelim.commonAndroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.os.BundleCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {
    private var otpListener: OtpListener? = null

    fun setOtpListener(listener: OtpListener) {
        this.otpListener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras ?: return
            val smsRetrieverStatus = BundleCompat.getParcelable(
                extras,
                SmsRetriever.EXTRA_STATUS,
                Status::class.java,
            ) ?: return
            val otp = when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val sms = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                    parseOtp(sms)
                }

                else -> null
            }
            otpListener?.onOtpReceived(otp)
        }
    }

    private fun parseOtp(sms: String?): String? {
        return sms?.split(" ")?.find { it.length == 6 && it.all { char -> char.isDigit() } }
    }

    fun interface OtpListener {
        fun onOtpReceived(value: String?)
    }
}
