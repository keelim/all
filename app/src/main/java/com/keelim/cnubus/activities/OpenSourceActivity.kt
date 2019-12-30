package com.keelim.cnubus.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.keelim.cnubus.R

class OpenSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)
        Toast.makeText(this, "기능 준비 중입니다. 잠시만 기다려 주세요", Toast.LENGTH_LONG).show()
    }

}
