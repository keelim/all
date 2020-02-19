package com.keelim.cnubus.view.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R


class RecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        val list: ArrayList<String> = ArrayList()
        for (i in 0..99) {
            list.add(String.format("TEXT %d", i))
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        val recyclerView = findViewById<RecyclerView>(R.id.recycler1)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        val adapter = RecyclerAdapter(list)
        recyclerView.adapter = adapter
    }

}


