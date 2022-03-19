package com.keelim.cnubus.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.UiContainer
import com.keelim.cnubus.databinding.ActivityLab4Binding

class Lab4Activity : AppCompatActivity() {
    private lateinit var binding:ActivityLab4Binding
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab4)

        setSupportActionBar(binding.toolbar)
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            val state = if (flag) {
                UiContainer(
                    getString(R.string.large_text)
                )
            } else {
                UiContainer(
                    "안녕하세요 반갑습니다."
                )
            }
            flag = !flag
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action") {
                    binding.container = state
                }.show()
        }
    }
}