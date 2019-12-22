package com.keelim.cnubus.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.R

class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // window 창 닫기
        setContentView(R.layout.activity_popup)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return event.action != MotionEvent.ACTION_OUTSIDE
    }

    fun mOnClose(view: View?) { //activity result
        val intent = Intent()
        intent.putExtra("result", "Close Popup")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
