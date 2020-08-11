package com.keelim.nandadiagnosis.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.keelim.nandadiagnosis.R
import kotlinx.android.synthetic.main.activity_open_source.*

class OpenSourceActivity : AppCompatActivity(R.layout.activity_open_source) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar_layout.title = title

    }
}