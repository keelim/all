package com.keelim.mygrade.ui.center.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.keelim.data.model.Result
import com.keelim.mygrade.BuildConfig
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.FragmentMainBinding
import com.keelim.mygrade.ui.GradeActivity
import com.keelim.mygrade.utils.ThemeManager
import com.keelim.mygrade.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var themeManager: ThemeManager
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        val ad = AdView(requireContext()).apply {
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
            startActivity((Intent(requireContext(), OssLicensesMenuActivity::class.java)))
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
            requireContext().toast("아직 기능을 준비합니다.")
        }
        notification.setOnClickListener{
            findNavController().navigate(R.id.notificationFragment)
        }
        
        history.setOnClickListener{
            findNavController().navigate(R.id.historyFragment)
        }
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            viewModel.save(
                                binding.valueOrigin.text.toString().toFloat(),
                                binding.valueAverage.text.toString().toFloat(),
                                binding.valueNumber.text.toString().toFloat(),
                                binding.valueStudent.text.toString().toInt(),
                                grade,
                                getLevel(
                                    (it.value * binding.valueStudent.text.toString()
                                        .toInt()) / 100
                                )
                            )
                            startActivity(
                                Intent(
                                    requireContext(),
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