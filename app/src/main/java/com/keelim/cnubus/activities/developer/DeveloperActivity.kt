package com.keelim.cnubus.activities.developer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.R

class DeveloperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)
        MaterialHelper.with(this).init().loadAbout()
    }
}