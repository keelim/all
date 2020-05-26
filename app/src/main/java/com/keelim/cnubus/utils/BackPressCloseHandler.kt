package com.keelim.cnubus.utils

import android.app.Activity
import android.widget.Toast


class BackPressCloseHandler(private val activity: Activity) {
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(
            activity, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

}