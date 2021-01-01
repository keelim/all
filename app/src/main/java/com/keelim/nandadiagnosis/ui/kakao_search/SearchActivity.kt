package com.keelim.nandadiagnosis.ui.kakao_search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.vm = searchViewModel
    }
}