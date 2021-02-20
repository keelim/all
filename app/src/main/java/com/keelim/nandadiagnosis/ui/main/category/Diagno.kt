/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.nandadiagnosis.ui.main.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.DiagnoBinding

class Diagno : AppCompatActivity() {
    private lateinit var binding: DiagnoBinding
    private var list: List<NandaEntity>? = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DiagnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra("extra")!!.toInt()
        list = AppDatabase.getInstance(this)!!.dataDao().get(number)

//        binding.list.adapter = DiagnosisRecyclerViewAdapter(list!!)
    }
}
