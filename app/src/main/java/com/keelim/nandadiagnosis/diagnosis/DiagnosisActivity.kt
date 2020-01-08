package com.keelim.nandadiagnosis.diagnosis

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.activities.WebViewActivity
import java.util.*
import kotlin.collections.ArrayList

class DiagnosisActivity : AppCompatActivity() {
    private var arrayList: ArrayList<DiagnosisItem>? = ArrayList()
    private var nav = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosislist)
        arrayList = ArrayList()
        arrayListSetting()
        val adapter = MyDiagnosisViewAdapter(this, arrayList)
        val listView = findViewById<ListView>(R.id.list)
        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long -> goWeb(nav + position + 1) }
    }

    private fun goWeb(total: Int) {
        val intent_web = Intent(this, WebViewActivity::class.java)
        intent_web.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/$total.html")
        startActivity(intent_web)
    }

    private fun arrayListSetting() {
        val array1 = resources.getStringArray(R.array.diagnosis1)
        val pointer = intent.getStringExtra("extra")
        when (Objects.requireNonNull(pointer)) {
            "1" -> {
                customAdd(0, 11, array1)
                nav = 0
            }
            "2" -> {
                customAdd(12, 22, array1)
                nav = 12
            }
            "3" -> {
                customAdd(23, 32, array1)
                nav = 23
            }
            "4" -> {
                customAdd(33, 51, array1)
                nav = 33
            }
            "5" -> {
                customAdd(52, 86, array1)
                nav = 52
            }
            "6" -> {
                customAdd(87, 97, array1)
                nav = 87
            }
            "7" -> {
                customAdd(98, 108, array1)
                nav = 98
            }
            "8" -> {
                customAdd(109, 123, array1)
                nav = 109
            }
            "9" -> {
                customAdd(124, 129, array1)
                nav = 124
            }
            "10" -> {
                customAdd(130, 168, array1)
                nav = 130
            }
            "11" -> {
                customAdd(169, 179, array1)
                nav = 169
            }
            "12" -> {
                customAdd(180, 207, array1)
                nav = 180
            }
            "13" -> {
                customAdd(208, 236, array1)
                nav = 208
            }
        }
    }

    private fun customAdd(startPoint: Int, finalPoint: Int, array1: Array<String>) {
        for (i in startPoint..finalPoint) {
            arrayList!!.add(DiagnosisItem(array1[i], ""))
        }
    }
}