package com.keelim.cnubus.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.R

class OpenSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)
        Toast.makeText(this, "오픈소스 기능 고지 입니다.", Toast.LENGTH_LONG).show()
    }

}
