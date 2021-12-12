package com.keelim.mygrade.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.keelim.mygrade.BuildConfig
import com.keelim.mygrade.data.Result
import com.keelim.mygrade.databinding.ActivityMainBinding
import com.keelim.mygrade.ui.GradeActivity
import com.keelim.mygrade.ui.center.CenterActivity
import com.keelim.mygrade.utils.ThemeManager
import com.keelim.mygrade.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        observeState()
    }

    private fun initViews() = with(binding) {
        val ad = AdView(this@MainActivity).apply {
            adSize = AdSize.BANNER
            adUnitId = if (BuildConfig.DEBUG.not()) {
                BuildConfig.key
            } else {
                "ca-app-pub-3940256099942544/6300978111"
            }
        }
        adView.addView(ad)
        val adRequest = AdRequest.Builder().build()
        ad.loadAd(adRequest)
        oss.setOnClickListener {
            startActivity((Intent(this@MainActivity, OssLicensesMenuActivity::class.java)))
        }
        btnSubmit.setOnClickListener {
            if (validation()) {
                viewModel.submit(
                    valueOrigin.text.toString().toFloat(),
                    valueAverage.text.toString().toFloat(),
                    valueNumber.text.toString().toFloat(),
                    valueStudent.text.toString().toInt(),
                    true
                )
            }
        }
        btnChange.setOnClickListener {
            if (themeManager.state == ThemeManager.ThemeMode.DARK) {
                themeManager.applyTheme(ThemeManager.ThemeMode.LIGHT)
                themeManager.state = ThemeManager.ThemeMode.LIGHT
            } else {
                themeManager.applyTheme(ThemeManager.ThemeMode.DARK)
                themeManager.state = ThemeManager.ThemeMode.DARK
            }
        }
        noAd.setOnClickListener {
            toast("아직 기능을 준비합니다.")
        }
        notification.setOnClickListener{
            startActivity(Intent(this@MainActivity, CenterActivity::class.java))
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
                        if (validation()) {
                            val grade = when {
                                it.value < 30 -> "A"
                                it.value < 60 -> "B"
                                it.value < 80 -> "C"
                                it.value < 100 -> "D"
                                else -> "F"
                            }

                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    GradeActivity::class.java
                                ).apply {
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
    
    private fun validation(): Boolean = with(binding){
        var flag = true
        if (valueOrigin.text.toString().isEmpty()) {
            valueOrigin.error = "원 점수를 입력해주세요"
            flag = false
        }
        if (valueAverage.text.toString().isEmpty()) {
            valueAverage.error = "평균 값을 입력해주세요"
            flag = false
        }
        if (valueNumber.text.toString().isEmpty()) {
            valueNumber.error = "표준 편차를 입력해주세요"
            flag = false
        }
        if (valueStudent.text.toString().isEmpty()) {
            valueStudent.error = "학생 수를 입력해주세요"
            flag = false
        }
        return flag
    }
}