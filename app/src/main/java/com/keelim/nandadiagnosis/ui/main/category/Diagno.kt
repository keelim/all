package com.keelim.nandadiagnosis.ui.main.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.DiagnoBinding

class Diagno: AppCompatActivity() {
    private lateinit var binding: DiagnoBinding
    private var list:List<NandaEntity>? = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DiagnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra("extra")!!.toInt()
        list = AppDatabase.getInstance(this)!!.dataDao().get(number)

//        binding.list.adapter = DiagnosisRecyclerViewAdapter(list!!)

        
    }
}