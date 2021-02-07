//package com.keelim.nandadiagnosis.ui.main.search
//
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import com.keelim.nandadiagnosis.R
//import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
//
//class TempActivity : AppCompatActivity() {
//    private lateinit var binding: FragmentSearchBinding
//    private val searchViewModel by viewModels<TempViewModel>()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
//        binding.lifecycleOwner = this
//        binding.vm = searchViewModel
//    }
//}