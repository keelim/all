package com.keelim.cnubus.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R
import com.keelim.cnubus.model.RecyclerImageTextAdapter
import com.keelim.cnubus.model.RecyclerItems



class TempActivity : AppCompatActivity() {
    private var mList: ArrayList<RecyclerItems> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        val list: ArrayList<String> = ArrayList()
        for (i in 0..99)
            list.add(String.format("TEXT %d", i))

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        val adapter = RecyclerImageTextAdapter(mList)
        recyclerView.adapter = adapter

        addItem(getDrawable(R.drawable.common_full_open_on_phone), "Box", "Account Box Black 36dp")
        // 두 번째 아이템 추가.
        addItem(getDrawable(R.drawable.common_full_open_on_phone), "Circle", "Account Circle Black 36dp")
        // 세 번째 아이템 추가.
        addItem(getDrawable(R.drawable.common_full_open_on_phone), "Ind", "Assignment Ind Black 36dp")

        adapter.notifyDataSetChanged()
    }

    fun addItem(icon: Drawable?, title: String?, desc: String?) {
        val item = RecyclerItems(icon!!, title!!, desc!!)
        mList.add(item)
    }
}


