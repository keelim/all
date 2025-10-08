package com.keelim.commonAndroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
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
