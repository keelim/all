package com.keelim.cnubus.error

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityErrorBinding


class ErrorActivity : AppCompatActivity() {
    val EXTRA_INTENT = "EXTRA_INTENT"
    val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"

    private lateinit var binding: ActivityErrorBinding

    private val lastActivityIntent by lazy {
        intent.getParcelableExtra<Intent>(EXTRA_INTENT)
    }

    private val errorText by lazy {
        intent.getStringExtra(EXTRA_ERROR_TEXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)

        binding.tvErrorLog.text = errorText

        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }

}