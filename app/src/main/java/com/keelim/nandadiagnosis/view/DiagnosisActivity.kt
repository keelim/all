package com.keelim.nandadiagnosis.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.model.MyDiagnosisViewAdapter
import kotlinx.android.synthetic.main.activity_diagnosislist.*

class DiagnosisActivity : AppCompatActivity(R.layout.activity_diagnosislist) {
    private var arrayList: ArrayList<MyDiagnosisViewAdapter.DiagnosisItem>? = ArrayList()
    private var nav = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrayListSetting()

        list.adapter = MyDiagnosisViewAdapter(this, arrayList)
        list.setOnItemClickListener { adapterView, view, i, l ->
            goWeb(nav + i + 1)
        }
    }

    private fun goWeb(total: Int) {
        Intent(this, WebViewActivity::class.java).apply {
            putExtra("URL", "https://keelim.github.io/nandaDiagnosis/$total.html")
            startActivity(this)
        }
    }

    private fun arrayListSetting() {
        val array1 = resources.getStringArray(R.array.diagnosis1)
        when (intent.getStringExtra("extra")) {
            "1" -> {
                nav = 0
                customAdd(nav, 11, array1) //ok 1~12
            }
            "2" -> {
                nav = 12
                customAdd(nav, 22, array1) // 13~23
            }
            "3" -> {
                nav = 23
                customAdd(nav, 32, array1) //24~33
            }
            "4" -> {
                nav = 33
                customAdd(nav, 51, array1) //34~52
            }
            "5" -> {
                nav = 52
                customAdd(nav, 86, array1) //53~87
            }
            "6" -> {
                nav = 87
                customAdd(nav, 97, array1) //88 ~98
            }
            "7" -> {
                nav = 98
                customAdd(nav, 108, array1) //99~109
            }
            "8" -> {
                nav = 109
                customAdd(nav, 123, array1) //110~124
            }
            "9" -> {
                nav = 124
                customAdd(nav, 129, array1) //125~130
            }
            "10" -> {
                nav = 130
                customAdd(nav, 167, array1) //131~168
            }
            "11" -> {
                nav = 168
                customAdd(nav, 178, array1) //169~179
            }
            "12" -> {
                nav = 179
                customAdd(nav, 205, array1) //180~206
            }
            "13" -> {
                nav = 204
                customAdd(nav, 223, array1) //207~236
            }
        }
    }

    private fun customAdd(startPoint: Int, finalPoint: Int, array1: Array<String>) {
        for (i in startPoint..finalPoint) {
            arrayList!!.add(MyDiagnosisViewAdapter.DiagnosisItem(array1[i], ""))
        }
    }
}