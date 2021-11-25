package com.keelim.mygrade

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.keelim.mygrade.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        observeState()
    }

    private fun initViews() = with(binding) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        oss.setOnClickListener {
            startActivity((Intent(this@MainActivity, OssLicensesMenuActivity::class.java)))
        }
        btnSubmit.setOnClickListener {
            viewModel.submit(
                valueOrigin.text.toString().toFloat(),
                valueAverage.text.toString().toFloat(),
                valueNumber.text.toString().toFloat(),
                valueStudent.text.toString().toInt(),
                true
            )
        }
    }

    private fun observeState() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect {
                when (it) {
                    is MainState.UnInitialized -> Unit
                    is MainState.Loading -> {
                        Snackbar.make(binding.root, "잠시만 기다려주세요", Snackbar.LENGTH_SHORT).show()
                    }
                    is MainState.Success -> {
                        val grade = when {
                            it.value < 30 -> "A"
                            it.value < 60 -> "B"
                            it.value < 80 -> "C"
                            it.value < 100 -> "D"
                            else -> "F"
                        }

                        startActivity(Intent(this@MainActivity, GradeActivity::class.java).apply {
                            putExtra(
                                "data", Result(
                                    grade,
                                    getLevel(
                                        (it.value * binding.valueStudent.text.toString()
                                            .toInt()) / 100
                                    )
                                )
                            )
                        })
                    }
                    is MainState.Error -> Snackbar.make(
                        binding.root,
                        "오류가 발생했습니다",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getLevel(level: Int): String = level.toString() + " / " + binding.valueStudent.text
}