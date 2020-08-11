package com.keelim.cnubus.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.cnubus.R
import com.keelim.cnubus.model.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_recycler.*


class RecyclerActivity : AppCompatActivity(R.layout.activity_recycler) {
    private lateinit var list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = ArrayList()

        for (i in 0..99) {
            list.add(String.format("TEXT %d", i))
        }

        recycler1.layoutManager = LinearLayoutManager(this)
        recycler1.adapter = RecyclerAdapter(list)
    }

}


